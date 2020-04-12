package com.mi.mvi.ui

interface UICommunicationListener {

    fun onUIMessageReceived(uiMessage: UIMessage)
}