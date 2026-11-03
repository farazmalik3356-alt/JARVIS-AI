package com.jarvis.ai.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsController @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var tts: TextToSpeech? = null
    private var initialized = false
    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready.asStateFlow()

    private var currentLanguage: Locale = Locale.US
    private var currentRate: Float = 1.0f

    fun init() {
        if (tts != null) return
        tts = TextToSpeech(context) { status ->
            initialized = status == TextToSpeech.SUCCESS
            if (initialized) {
                tts?.language = currentLanguage
                tts?.setSpeechRate(currentRate)
            }
            _ready.value = initialized
        }
    }

    fun setLanguage(tag: String) {
        val loc = runCatching { Locale.forLanguageTag(tag) }.getOrDefault(Locale.US)
        currentLanguage = loc
        runCatching { tts?.language = loc }
    }

    fun setRate(rate: Float) {
        currentRate = rate.coerceIn(0.5f, 2.0f)
        runCatching { tts?.setSpeechRate(currentRate) }
    }

    /**
     * Speak text. Returns a Flow<Boolean> that emits true on successful completion,
     * false on error/interruption. The caller can cancel the coroutine to stop.
     */
    fun speak(text: String): Flow<Boolean> = callbackFlow {
        if (text.isBlank()) { close(); return@callbackFlow }
        init()
        val engine = tts
        if (engine == null || !initialized) {
            trySend(false); close(); return@callbackFlow
        }
        val utterId = UUID.randomUUID().toString()
        engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) { trySend(true); close() }
            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) { trySend(false); close() }
            override fun onError(utteranceId: String?, errorCode: Int) { trySend(false); close() }
        })
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, utterId)
        awaitClose { engine.stop() }
    }

    fun stop() {
        runCatching { tts?.stop() }
    }

    fun shutdown() {
        runCatching { tts?.stop() }
        runCatching { tts?.shutdown() }
        tts = null
        initialized = false
        _ready.value = false
    }
}