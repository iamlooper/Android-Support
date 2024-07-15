package com.looper.android.support.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object SystemServiceUtils {

    /**
     * Copies the given text to the clipboard.
     *
     * @param context The context from which the clipboard service is obtained.
     * @param text The text to be copied to the clipboard.
     */
    fun copyToClipboard(context: Context, text: String) {
        // Get the clipboard service from the context.
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Create a clip data with the text.
        val clip = ClipData.newPlainText("text", text)

        // Set the clip data as the primary clip of the clipboard.
        clipboard.setPrimaryClip(clip)
    }

    /**
     * Shows the soft keyboard.
     *
     * @param editText The EditText to which the keyboard will be attached.
     * @param activity The activity from which the input method service is obtained.
     */
    fun showKeyboard(editText: EditText, activity: Activity) {
        // Request focus for the EditText.
        editText.requestFocus()

        // Get the InputMethodManager service.
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Show the keyboard.
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Hides the soft keyboard.
     *
     * @param activity The activity from which the input method service is obtained.
     */
    fun hideKeyboard(activity: Activity) {
        // Get the input method manager.
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus

        // If no view currently has focus, use root view, just so we can grab a window token from it.
        if (view == null) {
            view = activity.findViewById(android.R.id.content)
        }

        // Hide the keyboard.
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}
