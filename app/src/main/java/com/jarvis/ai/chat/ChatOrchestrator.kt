package com.jarvis.ai.chat

import com.jarvis.ai.action.ActionExecutor
import com.jarvis.ai.action.CommandPlan
import com.jarvis.ai.action.PlanParser
import com.jarvis.ai.action.PlanExecutionResult
import com.jarvis.ai.action.StepResult
import com.jarvis.ai.action.StepOutcome
import com.jarvis.ai.core.JarvisError
import com.jarvis.ai.core.Prompts
import com.jarvis.ai.data.local.db.entity.MessageEntity
import com.jarvis.ai.data.repository.ChatTurn
import com.jarvis.ai.data.repository.ConversationRepository
import com.jarvis.ai.data.repository.GeminiRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * High-level orchestration for a single conversation:
 *  1) Persist user message.
 *  2) Ask Gemini for a natural-language reply.
 *  3) Ask Gemini for a structured plan (best-effort).
 *  4) Return both to the caller; the caller decides whether to execute.
 */
@Singleton
class ChatOrchestrator @Inject constructor(
    private val geminiRepository: GeminiRepository,
    private val conversationRepository: ConversationRepository,
    private val planParser: PlanParser,
    private val actionExecutor: ActionExecutor
) {

    sealed class TurnEvent {
        data class UserPersisted(val msg: MessageEntity) : TurnEvent()
        data class ReplyToken(val text: String) : TurnEvent()
        data class ReplyPersisted(val msg: MessageEntity) : TurnEvent()
        data class PlanReady(val plan: CommandPlan) : TurnEvent()
        data class StepProgress(val result: StepResult) : TurnEvent()
        data class Completed(val result: PlanExecutionResult?) : TurnEvent()
        data class Failed(val error: JarvisError) : TurnEvent()
    }

    fun runTurn(
        conversationId: String,
        userText: String,
        history: List<ChatTurn>,
        autoExecuteSafe: Boolean
    ): Flow<TurnEvent> = flow {
        val userMsg = MessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            role = "user",
            content = userText,
            createdAt = System.currentTimeMillis()
        )
        conversationRepository.insertMessage(userMsg)
        emit(TurnEvent.UserPersisted(userMsg))

        // 1) Natural reply
        val replyText = try {
            geminiRepository.generate(
                systemPrompt = Prompts.SYSTEM,
                history = history,
                userMessage = userText
            )
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: JarvisError) {
            emit(TurnEvent.Failed(e))
            return@flow
        }

        emit(TurnEvent.ReplyToken(replyText))

        val replyMsg = MessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            role = "model",
            content = replyText,
            createdAt = System.currentTimeMillis()
        )
        conversationRepository.insertMessage(replyMsg)
        emit(TurnEvent.ReplyPersisted(replyMsg))

        // 2) Plan extraction (best-effort; failure is non-fatal)
        val planRaw = runCatching {
            geminiRepository.generate(
                systemPrompt = Prompts.PLAN_EXTRACTION,
                history = emptyList(),
                userMessage = userText
            )
        }.getOrNull() ?: ""
        val plan = planParser.parse(planRaw)

        if (plan.isEmpty) {
            emit(TurnEvent.Completed(null))
            return@flow
        }

        emit(TurnEvent.PlanReady(plan))

        if (!autoExecuteSafe) {
            emit(TurnEvent.Completed(null))
            return@flow
        }

        val results = mutableListOf<StepResult>()
        actionExecutor.execute(plan).collect { step ->
            emit(TurnEvent.StepProgress(step))
            results += step
            if (step.outcome is StepOutcome.AwaitingConfirmation) {
                // Caller must confirm; halt here for safety.
                return@collect
            }
        }
        emit(TurnEvent.Completed(PlanExecutionResult(results)))
    }.catch { e ->
        emit(TurnEvent.Failed(
            if (e is JarvisError) e else JarvisError.Unknown(e)
        ))
    }

    fun executeConfirmed(plan: CommandPlan, confirmedIndices: Set<Int>): Flow<StepResult> =
        actionExecutor.execute(plan, confirmedIndices)
}