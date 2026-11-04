package com.jarvis.ai.data.repository

import com.jarvis.ai.core.JarvisError
import com.jarvis.ai.data.remote.GeminiApi
import com.jarvis.ai.data.remote.dto.Content
import com.jarvis.ai.data.remote.dto.GeminiRequest
import com.jarvis.ai.data.remote.dto.GenerationConfig
import com.jarvis.ai.data.remote.dto.Part
import com.jarvis.ai.data.remote.dto.SafetySetting
import com.jarvis.ai.di.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class GeminiRepositoryImpl @Inject constructor(
    private val api: GeminiApi,
    private val config: AppConfig
) : GeminiRepository {

    override suspend fun generate(
        systemPrompt: String,
        history: List<ChatTurn>,
        userMessage: String
    ): String = withContext(Dispatchers.IO) {
        val contents = buildContents(history, userMessage)
        val request = GeminiRequest(
            contents = contents,
            systemInstruction = if (systemPrompt.isBlank()) null
            else Content(role = "system", parts = listOf(Part(text = systemPrompt))),
            generationConfig = GenerationConfig(
                temperature = 0.7f,
                topP = 0.95f,
                topK = 40,
                maxOutputTokens = 2048
            ),
            safetySettings = defaultSafety()
        )
        val response = try {
            api.generate(model = config.geminiModel, body = request)
        } catch (e: SocketTimeoutException) {
            throw JarvisError.Timeout()
        } catch (e: UnknownHostException) {
            throw JarvisError.Network(e)
        } catch (e: IOException) {
            throw JarvisError.Network(e)
        }

        if (!response.isSuccessful) {
            val body = runCatching { response.errorBody()?.string() }.getOrNull()
            throw mapHttpError(response.code(), body ?: "")
        }
        val parsed = response.body() ?: throw JarvisError.MalformedResponse()
        parsed.error?.let {
            throw JarvisError.ServerError(it.code ?: -1, it.message ?: "Gemini error")
        }
        val text = parsed.candidates
            ?.firstOrNull()?.content?.parts
            ?.mapNotNull { it.text }
            ?.joinToString("")
            ?.trim()
        if (text.isNullOrBlank()) {
            val blockReason = parsed.promptFeedback?.blockReason
            throw if (blockReason != null)
                JarvisError.MalformedResponse(
                    IllegalStateException("Blocked: $blockReason")
                )
            else JarvisError.MalformedResponse()
        }
        text
    }

    override suspend fun verifyApiKey(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val res = api.listModels()
            if (res.isSuccessful) Result.success(Unit)
            else {
                val code = res.code()
                val mapped = when (code) {
                    400, 401, 403 -> JarvisError.InvalidApiKey()
                    429 -> JarvisError.RateLimited()
                    in 500..599 -> JarvisError.ServerError(code, "server")
                    else -> JarvisError.Unknown(IllegalStateException("HTTP $code"))
                }
                Result.failure(mapped)
            }
        } catch (e: UnknownHostException) {
            Result.failure(JarvisError.Network(e))
        } catch (e: IOException) {
            Result.failure(JarvisError.Network(e))
        } catch (e: JarvisError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(JarvisError.Unknown(e))
        }
    }

    override fun generateStream(
        systemPrompt: String,
        history: List<ChatTurn>,
        userMessage: String
    ): Flow<GeminiRepository.StreamEvent> = flow<GeminiRepository.StreamEvent> {
        try {
            val text = generate(systemPrompt, history, userMessage)
            emit(GeminiRepository.StreamEvent.Token(text))
            emit(GeminiRepository.StreamEvent.Done)
        } catch (e: JarvisError) {
            emit(GeminiRepository.StreamEvent.Failed(e))
        } catch (e: Exception) {
            emit(GeminiRepository.StreamEvent.Failed(JarvisError.Unknown(e)))
        }
    }.flowOn(Dispatchers.IO)

    private fun buildContents(history: List<ChatTurn>, userMessage: String): List<Content> {
        val list = mutableListOf<Content>()
        history.forEach { t ->
            val role = if (t.role == "user") "user" else "model"
            if (t.text.isNotBlank()) {
                list += Content(role = role, parts = listOf(Part(text = t.text)))
            }
        }
        list += Content(role = "user", parts = listOf(Part(text = userMessage)))
        return list
    }

    private fun mapHttpError(code: Int, body: String): JarvisError = when (code) {
        400, 401, 403 -> JarvisError.InvalidApiKey()
        404 -> JarvisError.ServerError(404, "model not found")
        408, 504 -> JarvisError.Timeout()
        429 -> JarvisError.RateLimited()
        in 500..599 -> JarvisError.ServerError(code, body.take(200))
        else -> JarvisError.Unknown(IllegalStateException("HTTP $code"))
    }

    private fun defaultSafety(): List<SafetySetting> = listOf(
        SafetySetting("HARM_CATEGORY_HARASSMENT", "BLOCK_ONLY_HIGH"),
        SafetySetting("HARM_CATEGORY_HATE_SPEECH", "BLOCK_ONLY_HIGH"),
        SafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "BLOCK_ONLY_HIGH"),
        SafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_ONLY_HIGH")
    )
}
