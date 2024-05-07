package com.looper.android.support.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

object UIUtils {

    const val TOAST_LENGTH_SHORT: Int = 2000
    const val TOAST_LENGTH_LONG: Int = 3500
    const val SNACKBAR_LENGTH_SHORT: Int = 4000
    const val SNACKBAR_LENGTH_LONG: Int = 10000

    /**
     * Displays a toast message.
     *
     * @param context, the context in which the toast should be displayed.
     * @param message, the message to be displayed in the toast.
     * @param duration, the duration for which the toast should be shown, either [TOAST_LENGTH_SHORT] or [TOAST_LENGTH_LONG].
     */
    fun showToast(context: Context, message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }

    /**
     * Displays a snackbar notification.
     *
     * @param view, the view to which the snackbar should be attached.
     * @param message, the message to be displayed in the snackbar.
     * @param duration, the duration for which the snackbar should be shown, either [SNACKBAR_LENGTH_SHORT] or [SNACKBAR_LENGTH_LONG].
     */
    fun showSnackbar(view: View, message: String, duration: Int) {
        Snackbar.make(view, message, duration).show()
    }

    /**
     * Displays a snackbar notification with an action.
     *
     * @param view, the view to which the snackbar should be attached.
     * @param message, the message to be displayed in the snackbar.
     * @param actionText, the text to be displayed for the action button in the snackbar.
     * @param duration, the duration for which the snackbar should be shown, either [SNACKBAR_LENGTH_SHORT] or [SNACKBAR_LENGTH_LONG].
     * @param action, the action to be performed when the action button in the snackbar is clicked.
     */
    fun showSnackbarWithAction(view: View, message: String, actionText: String, duration: Int, action: () -> Unit) {
        Snackbar.make(view, message, duration)
            .setAction(actionText) {
                action.invoke()
            }
            .show()
    }
    
    /**
     * Dismisses a dialog.
     *
     * Checks if the dialog is not null and is being shown, and then dismisses the dialog using the `dismiss()` function.     
     *
     * @param dialog, the dialog to dismiss.
     */
    fun dismissDialog(dialog: AlertDialog) {
        dialog.takeIf { it.isShowing }?.dismiss()
    }    
}