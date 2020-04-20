package com.mi.mvi.presentation.common

import com.mi.mvi.utils.StateMessage

interface UICommunicationListener {
    fun onUIMessageReceived(stateMessage: StateMessage)
    fun hideSoftKeyboard()
    fun isStoragePermissionGranted(): Boolean
    fun displayLoading(isLoading: Boolean)
}
