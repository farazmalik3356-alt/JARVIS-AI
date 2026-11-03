package com.jarvis.ai.voice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceCoordinator @Inject constructor(
    private val speechController: SpeechController,
    private val ttsController: TtsController
) {
    private val _state = MutableStateFlow<VoiceState>(VoiceState.Idle)
    val state: StateFlow<VoiceState> = _state.asStateFlow()

    private var listenJob: Job? = null
    private var speakJob: Job? = null

    fun isSpeechAvailable(): Boolean = speechController.isAvailable()

    fun startListening(
        scope: CoroutineScope,
        languageTag: String,
        onFinalText: (String) -> Unit
    ) {
        if (!speechController.isAvailable()) {
            _state.value = VoiceState.Error("Speech recognition unavailable on this device.")
            return
        }
        stopListening()
        _state.value = VoiceState.Listening
        listenJob = scope.launch {
            speechController.listen(languageTag).collect { event ->
                when (event) {
                    is SpeechController.Event.Ready -> _state.value = VoiceState.Listening
                    is SpeechController.Event.Partial ->
                        _state.value = VoiceState.Processing(event.text)
                    is SpeechController.Event.EndOfSpeech -> { /* keep last partial */ }
                    is SpeechController.Event.Result -> {
                        _state.value = VoiceState.Processing(event.text)
                        if (event.text.isNotBlank()) onFinalText(event.text)
                        _state.value = VoiceState.Idle
                    }
                    is SpeechController.Event.Error -> {
                        _state.value = VoiceState.Error(event.message)
                    }
                }
            }
        }
    }

    fun stopListening() {
        listenJob?.cancel()
        listenJob = null
        if (_state.value is VoiceState.Listening || _state.value is VoiceState.Processing) {
            _state.value = VoiceState.Idle
        }
    }

    fun speak(scope: CoroutineScope, text: String) {
        speakJob?.cancel()
        speakJob = scope.launch {
            _state.value = VoiceState.Speaking(text)
            ttsController.speak(text).collect { ok ->
                if (!ok) _state.value = VoiceState.Error("Speech playback failed.")
            }
            if (_state.value is VoiceState.Speaking) {
                _state.value = VoiceState.Idle
            }
        }
    }

    fun stopSpeaking() {
        speakJob?.cancel()
        speakJob = null
        ttsController.stop()
        if (_state.value is VoiceState.Speaking) _state.value = VoiceState.Idle
    }

    fun shutdown() {
        stopListening()
        stopSpeaking()
        ttsController.shutdown()
    }
}