package com.jarvis.ai.data.remote

import com.jarvis.ai.core.JarvisError
import com.jarvis.ai.data.local.secure.SecureKeyStore
import okhttp3.Interceptor
import okhttp3.Response

class GeminiInterceptor(
    private val secureKeyStore: SecureKeyStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val key = secureKeyStore.readApiKey()
            ?: throw JarvisError.NoApiKey()
        val request = chain.request().newBuilder()
            .addHeader("x-goog-api-key", key)
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}