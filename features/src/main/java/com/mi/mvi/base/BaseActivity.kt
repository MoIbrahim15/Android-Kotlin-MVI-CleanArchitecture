package com.mi.mvi.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mi.mvi.common.*
import com.mi.mvi.common.SessionManager
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.datastate.StateMessage
import com.mi.mvi.domain.datastate.UIComponentType
import com.mi.mvi.utils.Constants.Companion.PERMISSION_REQUEST_READ_STORAGE
import org.koin.android.ext.android.inject

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId),
    DataStateChangeListener,
    UICommunicationListener {

    val sessionManager: SessionManager by inject()

    override fun onUIMessageReceived(stateMessage: StateMessage) {
        when (stateMessage.uiComponentType) {
            is UIComponentType.AreYouSureDialog -> {
                areYouSureDialog(stateMessage.message, (stateMessage.uiComponentType as UIComponentType.AreYouSureDialog).callBack)
            }
            is UIComponentType.DIALOG -> {
                displayInfoDialog(stateMessage.message)
            }
            is UIComponentType.TOAST -> {
                displayToast(stateMessage.message)
            }
            is UIComponentType.NONE -> {
            }
        }
    }

    override fun onDataStateChangeListener(dataState: DataState<*>?) {
        dataState?.let {
            displayLoading(it.loading)
            it.stateMessage?.let { stateMessage ->
                handleResponseState(stateMessage)
            }
        }
    }

    private fun handleResponseState(stateMessage: StateMessage?) {
        stateMessage?.message?.let { message ->
            when (stateMessage.uiComponentType) {
                is UIComponentType.DIALOG -> {
                    displayErrorDialog(message)
                }
                is UIComponentType.TOAST -> {
                    displayToast(message)
                }
            }
        }
    }

    override fun hideSoftKeyboard() {
        currentFocus?.let { currentFocus ->
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    override fun isStoragePermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_READ_STORAGE
            )
            return false
        } else {
            return true
        }
    }
}
