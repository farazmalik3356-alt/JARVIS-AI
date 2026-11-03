package com.jarvis.ai.data

import com.google.common.truth.Truth.assertThat
import com.jarvis.ai.core.JarvisError
import com.jarvis.ai.data.remote.GeminiApi
import com.jarvis.ai.data.repository.ChatTurn
import com.jarvis.ai.data.repository.GeminiRepositoryImpl
import com.jarvis.ai.di.AppConfig
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import org.junit.After
import org.junit.Before
import org.junit.Test

class GeminiRepositoryTest {

    private lateinit var server: MockWebServer
    private lateinit var repository: GeminiRepositoryImpl
    private lateinit var config: AppConfig

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()
        val json = Json { ignoreUnknownKeys = true }
        val api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(GeminiApi::class.java)
        config = AppConfig(
            geminiModel = "gemini-1.5-flash",
            geminiBaseUrl = server.url("/").toString(),
            isDebug = true
        )
        repository = GeminiRepositoryImpl(api, config)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun success_returnsText() = runTest {
        val body = """
            {"candidates":[{"content":{"role":"model","parts":[{"text":"Hello, Sir."}]}}]}
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val text = repository.generate(
            systemPrompt = "Be brief",
            history = emptyList(),
            userMessage = "Hi"
        )
        assertThat(text).isEqualTo("Hello, Sir.")
    }

    @Test
    fun http401_mapsToInvalidKey() = runTest {
        server.enqueue(MockResponse().setResponseCode(401).setBody("{}"))
        try {
            repository.generate("s", emptyList(), "hi")
            error("expected throw")
        } catch (e: JarvisError.InvalidApiKey) {
            // expected
        }
    }

    @Test
    fun http429_mapsToRateLimited() = runTest {
        server.enqueue(MockResponse().setResponseCode(429).setBody("{}"))
        try {
            repository.generate("s", emptyList(), "hi")
            error("expected throw")
        } catch (e: JarvisError.RateLimited) {
            // expected
        }
    }

    @Test
    fun http500_mapsToServerError() = runTest {
        server.enqueue(MockResponse().setResponseCode(500).setBody("boom"))
        try {
            repository.generate("s", emptyList(), "hi")
            error("expected throw")
        } catch (e: JarvisError.ServerError) {
            assertThat(e.code).isEqualTo(500)
        }
    }

    @Test
    fun malformedBody_throwsMalformed() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))
        try {
            repository.generate("s", emptyList(), "hi")
            error("expected throw")
        } catch (e: JarvisError.MalformedResponse) {
            // expected
        }
    }

    @Test
    fun historyOrdering_isPreserved() = runTest {
        val body = """
            {"candidates":[{"content":{"parts":[{"text":"ok"}]}}]}
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))
        repository.generate(
            "s",
            listOf(ChatTurn("user", "first"), ChatTurn("model", "second")),
            "third"
        )
        val recorded = server.takeRequest().body.readUtf8()
        val idx1 = recorded.indexOf("first")
        val idx2 = recorded.indexOf("second")
        val idx3 = recorded.indexOf("third")
        assertThat(idx1).isLessThan(idx2)
        assertThat(idx2).isLessThan(idx3)
    }

    @Test
    fun verifyApiKey_success() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody("""{"models":[]}"""))
        val result = repository.verifyApiKey()
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun verifyApiKey_invalidKey() = runTest {
        server.enqueue(MockResponse().setResponseCode(400).setBody("{}"))
        val result = repository.verifyApiKey()
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(JarvisError.InvalidApiKey::class.java)
    }
}
