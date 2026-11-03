package com.jarvis.ai.core

sealed class JarvisError(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NoApiKey : JarvisError("No API key configured.")
    class InvalidApiKey : JarvisError("Gemini rejected the API key.")
    class RateLimited(retryAfterSec: Long? = null) :
        JarvisError("Rate limited${retryAfterSec?.let { " (retry in ${it}s)" } ?: ""}.") {
        val retryAfter = retryAfterSec
    }
    class Network(cause: Throwable? = null) : JarvisError("Network error.", cause)
    class Timeout : JarvisError("Request timed out.")
    class MalformedResponse(cause: Throwable? = null) :
        JarvisError("Unexpected response from Gemini.", cause)
    class ServerError(val code: Int, msg: String) : JarvisError("Server $code: $msg")
    class Cancelled : JarvisError("Cancelled.")
    class Unknown(cause: Throwable? = null) : JarvisError(cause?.message ?: "Unknown error.", cause)

    fun userMessage(): String = when (this) {
        is NoApiKey -> "No API key configured. Add one in Settings."
        is InvalidApiKey -> "Gemini rejected the API key. Update it in Settings."
        is RateLimited -> "Rate limited by Gemini. Try again shortly."
        is Network -> "Network unavailable. Check connection."
        is Timeout -> "Request timed out. Please retry."
        is MalformedResponse -> "Received an unexpected response from Gemini."
        is ServerError -> "Gemini server error ($code)."
        is Cancelled -> "Request cancelled."
        is Unknown -> "Something went wrong."
    }
}