package com.mi.mvi.presentation

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.mi.mvi.R

fun Activity.displayToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.displayToast(@StringRes msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.displaySuccessDialog(message: String) {
    MaterialDialog(this)
        .show {
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayErrorDialog(message: String) {
    MaterialDialog(this)
        .show {
            title(R.string.text_error)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayInfoDialog(message: String) {
    MaterialDialog(this)
        .show {
            title(R.string.are_you_sure)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.areYouSureDialog(message: String, callback: AreYouSureCallBack) {
    MaterialDialog(this)
        .show {
            title(R.string.text_info)
            message(text = message)
            negativeButton(R.string.text_cancel) {
                callback.cancel()
            }
            positiveButton(R.string.text_yes) {
                callback.proceed()
            }
        }
}


interface AreYouSureCallBack {
    fun proceed()
    fun cancel()
}