package com.mi.mvi.presentation.common

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.mi.mvi.R

fun Activity.displayToast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.displayToast(@StringRes msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

//fixed leak memory
var materialDialog: MaterialDialog? = null

fun Activity.displaySuccessDialog(message: String) {
    materialDialog = MaterialDialog(this)
        .show {
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayErrorDialog(message: String) {
    materialDialog = MaterialDialog(this)
        .show {
            title(R.string.text_error)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displayInfoDialog(message: String?) {
    materialDialog = MaterialDialog(this)
        .show {
            title(R.string.are_you_sure)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.areYouSureDialog(message: String?, callback: AreYouSureCallBack) {
    materialDialog = MaterialDialog(this)
        .show {
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_yes) {
                callback.proceed()

            }
            negativeButton(R.string.text_cancel) {
                callback.cancel()
                materialDialog = null
            }
        }
}

interface AreYouSureCallBack {
    fun proceed()
    fun cancel()
}