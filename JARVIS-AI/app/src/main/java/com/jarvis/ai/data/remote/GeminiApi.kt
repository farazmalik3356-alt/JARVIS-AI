package com.jarvis.ai.data.remote

import com.jarvis.ai.data.remote.dto.GeminiRequest
import com.jarvis.ai.data.remote.dto.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GeminiApi {

    @POST("v1beta/models/{model}:generateContent")
    suspend fun generate(
        @Path("model") model: String,
        @Body body: GeminiRequest,
        @Query("alt") alt: String = "json"
    ): Response<GeminiResponse>

    @GET("v1beta/models")
    suspend fun listModels(): Response<com.jarvis.ai.data.remote.dto.ModelsListResponse>
}