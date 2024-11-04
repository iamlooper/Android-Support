package com.looper.android.support.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.looper.android.support.R

object DialogUtil {

    /**
     * Displays a loading progress dialog.
     *
     * @param context The context in which the dialog should be displayed.
     * @return The displayed AlertDialog instance.
     */
    fun displayLoadingProgressDialog(context: Context): AlertDialog {
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(R.layout.dialog_loading_progress)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()

        return dialog
    }

    /**
     * Displays an action confirmation dialog with a custom title and optional positive and negative action callbacks.
     *
     * @param context The context in which the dialog should be displayed.
     * @param title The title to be displayed in the dialog.
     * @param onPositiveAction Optional callback for the positive action button.
     * @param onNegativeAction Optional callback for the negative action button.
     * @return The displayed AlertDialog instance.
     */
    fun displayActionConfirmDialog(
        context: Context,
        title: String,
        onPositiveAction: (() -> Unit)? = null,
        onNegativeAction: (() -> Unit)? = null
    ): AlertDialog {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(context.getString(R.string.action_confirm_text))
            .setPositiveButton(context.getString(R.string.okay)) { _, _ ->
                onPositiveAction?.invoke()
            }
            .setNegativeButton(context.getString(R.string.cancel)) { _, _ ->
                onNegativeAction?.invoke()
            }
            .create()

        dialog.show()

        return dialog
    }

    /**
     * Displays an edit text dialog with a custom title, initial input, and optional positive and negative action callbacks.
     *
     * @param context The context in which the dialog should be displayed.
     * @param title The title to be displayed in the dialog.
     * @param initialInput The initial input text to be displayed in the edit text field.
     * @param onPositiveAction Optional callback for the positive action button.
     * @param onNegativeAction Optional callback for the negative action button.
     * @return The displayed AlertDialog instance.
     */
    @SuppressLint("InflateParams")
    fun displayEditTextDialog(
        context: Context,
        title: String,
        initialInput: String = "",
        onPositiveAction: ((input: TextInputEditText) -> Unit)? = null,
        onNegativeAction: ((input: TextInputEditText) -> Unit)? = null
    ): AlertDialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null)
        val input: TextInputEditText = dialogView.findViewById(R.id.input)

        input.setText(initialInput)

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton(context.getString(R.string.okay)) { _, _ ->
                onPositiveAction?.invoke(input)
            }
            .setNegativeButton(context.getString(R.string.cancel)) { _, _ ->
                onNegativeAction?.invoke(input)
            }
            .create()

        dialog.show()

        return dialog
    }
}