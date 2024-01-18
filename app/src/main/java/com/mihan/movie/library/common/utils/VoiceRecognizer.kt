package com.mihan.movie.library.common.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.mihan.movie.library.common.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class VoiceRecognizerState(
    val spokenText: String = Constants.EMPTY_STRING,
    val isSpeaking: Boolean = false,
    val error: String = Constants.EMPTY_STRING
)

class VoiceRecognizer @Inject constructor(context: Context) : RecognitionListener {

    private val _voiceRecognizerState = MutableStateFlow(VoiceRecognizerState())
    val voiceRecognizerState = _voiceRecognizerState.asStateFlow()
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    fun startListening(languageCode: String) {
        _voiceRecognizerState.update { VoiceRecognizerState() }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }
        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)
        _voiceRecognizerState.update { it.copy(isSpeaking = true) }
    }

    fun stopListening() {
        _voiceRecognizerState.update { it.copy(isSpeaking = false) }
        recognizer.stopListening()
    }

    fun resetState() {
        _voiceRecognizerState.update { VoiceRecognizerState() }
    }

    override fun onReadyForSpeech(params: Bundle?) = Unit

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() = stopListening()

    override fun onError(error: Int) {
        when (error) {
            SpeechRecognizer.ERROR_CLIENT -> return

            SpeechRecognizer.ERROR_NO_MATCH -> {
                _voiceRecognizerState.update { it.copy(isSpeaking = false, error = "Голос не распознан") }
                recognizer.stopListening()
            }

            else -> {
                _voiceRecognizerState.update {
                    it.copy(isSpeaking = false, error = "Голосовой ввод недоступен. Код ошибки $error")
                }
                recognizer.stopListening()
            }
        }
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { spokenText ->
                _voiceRecognizerState.update { it.copy(spokenText = spokenText) }
            }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}