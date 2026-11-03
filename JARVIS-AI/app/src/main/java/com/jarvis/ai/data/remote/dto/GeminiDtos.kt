package com.jarvis.ai.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null,
    val generationConfig: GenerationConfig? = null,
    val safetySettings: List<SafetySetting>? = null
)

@Serializable
data class Content(
    val role: String? = null,
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String? = null
)

@Serializable
data class GenerationConfig(
    val temperature: Float? = null,
    val topP: Float? = null,
    val topK: Int? = null,
    val maxOutputTokens: Int? = null,
    val responseMimeType: String? = null
)

@Serializable
data class SafetySetting(
    val category: String,
    val threshold: String
)

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null,
    val promptFeedback: PromptFeedback? = null,
    val error: GeminiErrorBody? = null,
    @SerialName("usageMetadata") val usage: UsageMetadata? = null
)

@Serializable
data class Candidate(
    val content: Content? = null,
    @SerialName("finishReason") val finishReason: String? = null,
    val index: Int? = null
)

@Serializable
data class PromptFeedback(
    @SerialName("blockReason") val blockReason: String? = null
)

@Serializable
data class GeminiErrorBody(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null
)

@Serializable
data class UsageMetadata(
    @SerialName("promptTokenCount") val promptTokens: Int? = null,
    @SerialName("candidatesTokenCount") val candidatesTokens: Int? = null,
    @SerialName("totalTokenCount") val totalTokens: Int? = null
)

@Serializable
data class ModelsListResponse(val models: List<ModelInfo>? = null)

@Serializable
data class ModelInfo(
    val name: String,
    @SerialName("displayName") val displayName: String? = null,
    @SerialName("supportedGenerationMethods") val methods: List<String>? = null
)