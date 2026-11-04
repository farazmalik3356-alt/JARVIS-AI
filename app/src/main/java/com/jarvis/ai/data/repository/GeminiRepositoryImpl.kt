package com.jarvis.ai.data.repository

import com.jarvis.ai.core.JarvisError
import com.jarvis.ai.data.remote.GeminiApi
import com.jarvis.ai.di.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class GeminiRepositoryImpl @Inject constructor(
    private val api: GeminiApi,
    private val config: AppConfig,
    private val client: OkHttpClient
) : GeminiRepository {

    override suspend fun generate(
        systemPrompt: String,
        history: List<ChatTurn>,
        userMessage: String
    ): String = withContext(Dispatchers.IO) {
        val messages = JSONArray()
        if (systemPrompt.isNotBlank()) {
            messages.put(JSONObject().put("role", "system").put("content", systemPrompt))
        }
        history.forEach { t ->
            messages.put(JSONObject().put("role", if(t.role == "user") "user" else "assistant").put("content", t.text))
        }
        messages.put(JSONObject().put("role", "user").put("content", userMessage))

        val body = JSONObject()
            .put("model", config.geminiModel)
            .put("messages", messages)
            .toString()

        val request = Request.Builder()
            .url("https://openrouter.ai/api/v1/chat/completions")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()

        val response = try {
            client.newCall(request).execute()
        } catch (e: SocketTimeoutException) {
            throw JarvisError.Timeout()
        } catch (e: UnknownHostException) {
            throw JarvisError.Network(e)
        } catch (e: IOException) {
            throw JarvisError.Network(e)
        }

        if (!response.isSuccessful) {
            val errorBody = response.body?.string() ?: ""
            throw JarvisError.ServerError(response.code, errorBody.take(200))
        }

        val responseBody = response.body?.string() ?: throw JarvisError.MalformedResponse()
        val json = JSONObject(responseBody)
        val content = json.getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")
        
        if (content.isBlank()) throw JarvisError.MalformedResponse()
        content
    }

    override suspend fun verifyApiKey(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // OpenRouter ki key verify karne ke liye hum models list call karte hain
            val request = Request.Builder()
                .url("https://openrouter.ai/api/v1/models")
                .get()
                .build()
            
            val response = client.newCall(request).execute()
            if (response.isSuccessful) Result.success(Unit)
            else {
                val code = response.code()
                val mapped = when (code) {
                    401, 403 -> JarvisError.InvalidApiKey()
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
    ): Flow<StreamEvent> = flow {
        try {
            val text = generate(systemPrompt, history, userMessage)
            emit(StreamEvent.Token(text))
            emit(StreamEvent.Done)
        } catch (e: JarvisError) {
            emit(StreamEvent.Failed(e))
        } catch (e: Exception) {
            emit(StreamEvent.Failed(JarvisError.Unknown(e)))
        }
    }.flowOn(Dispatchers.IO)
}
