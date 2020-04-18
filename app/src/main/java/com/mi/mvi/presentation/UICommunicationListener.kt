package com.mi.mvi.presentation

import com.mi.mvi.utils.response_handler.StateMessage


interface UICommunicationListener {
    fun onUIMessageReceived(stateMessage: StateMessage)
    fun hideSoftKeyboard()
    fun isStoragePermissionGranted(): Boolean
    fun displayLoading(isLoading: Boolean)
}