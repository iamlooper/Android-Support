package com.looper.android.support.util

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.*
import com.looper.android.support.R

class SpeechUtils(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null

    // Initialize Text-to-Speech.
    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(context, context.getString(R.string.tts_init_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Initiates speech recognition.
     *
     * @param listener, the listener that will receive the recognition results.
     */
    fun initSpeechToText(listener: RecognitionListener) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(listener)

            val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            speechRecognizer?.startListening(speechIntent)
        } else {
            // Speech recognition not available.
            Toast.makeText(context, context.getString(R.string.stt_not_available), Toast.LENGTH_SHORT).show()
        }
    }

    // Starts speech recognition.
    fun startListening() {
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        speechRecognizer?.startListening(speechIntent)
    }

    /**
     * Speaks the provided text using the TextToSpeech engine.
     *
     * @param text The text to be spoken.
     */
    fun speak(text: String) {
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    // Stop text-to-speech.
    fun stopTextToSpeech() {
        textToSpeech?.stop()
    }

    // Clean up resources.
    fun destroy() {
        speechRecognizer?.destroy()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}
