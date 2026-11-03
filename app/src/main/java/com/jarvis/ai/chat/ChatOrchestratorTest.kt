package com.jarvis.ai.chat

import com.google.common.truth.Truth.assertThat
import com.jarvis.ai.action.ActionExecutor
import com.jarvis.ai.action.CommandPlan
import com.jarvis.ai.action.PlanParser
import com.jarvis.ai.action.StepOutcome
import com.jarvis.ai.action.StepResult
import com.jarvis.ai.core.JarvisError
import com.jarvis.ai.data.local.db.entity.ConversationEntity
import com.jarvis.ai.data.local.db.entity.MessageEntity
import com.jarvis.ai.data.repository.ConversationRepository
import com.jarvis.ai.data.repository.GeminiRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ChatOrchestratorTest {

    private val gemini: GeminiRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk(relaxed = true)
    private val planParser = PlanParser()
    private val executor: ActionExecutor = mockk(relaxed = true)

    private fun orchestrator() = ChatOrchestrator(
        geminiRepository = gemini,
        conversationRepository = conversationRepository,
        planParser = planParser,
        actionExecutor = executor
    )

    @Test
    fun geminiFailure_emitsFailed() = runTest {
        coEvery {
            gemini.generate(any(), any(), any())
        } throws JarvisError.Network(IllegalStateException("offline"))

        val events = orchestrator().runTurn(
            conversationId = "c1",
            userText = "hi",
            history = emptyList(),
            autoExecuteSafe = true
        ).toList()

        assertThat(events.any { it is ChatOrchestrator.TurnEvent.UserPersisted }).isTrue()
        assertThat(events.last()).isInstanceOf(ChatOrchestrator.TurnEvent.Failed::class.java)
    }

    @Test
    fun successfulReplyNoPlan_completes() = runTest {
        coEvery {
            gemini.generate(any(), any(), any())
        } returns "Hello"
        // second call returns empty plan
        coEvery {
            gemini.generate(match { it.contains("command planner") }, any(), any())
        } returns "{}"

        val events = orchestrator().runTurn("c1", "hi", emptyList(), true).toList()
        assertThat(events.any { it is ChatOrchestrator.TurnEvent.ReplyToken }).isTrue()
        assertThat(events.any { it is ChatOrchestrator.TurnEvent.Completed }).isTrue()
    }

    @Test
    fun invalidPlanIsNotExecuted() = runTest {
        coEvery {
            gemini.generate(any(), any(), any())
        } returns "ok"
        coEvery {
            gemini.generate(match { it.contains("command planner") }, any(), any())
        } returns """{"actions":[{"type":"run_shell","params":{"cmd":"rm -rf"}}]}"""

        every { executor.execute(any(), any()) } returns flowOf()

        val events = orchestrator().runTurn("c1", "test", emptyList(), true).toList()
        // No plan should be produced because all actions were rejected
        assertThat(events.any { it is ChatOrchestrator.TurnEvent.PlanReady }).isFalse()
    }
}