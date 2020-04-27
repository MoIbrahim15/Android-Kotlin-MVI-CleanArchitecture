package com.mi.mvi.features.common

import com.mi.mvi.domain.datastate.StateMessage

interface UICommunicationListener {
    fun onUIMessageReceived(stateMessage: StateMessage)
    fun hideSoftKeyboard()
    fun isStoragePermissionGranted(): Boolean
    fun displayLoading(isLoading: Boolean)
}
