package com.mi.mvi.presentation

interface UICommunicationListener {

    fun onUIMessageReceived(uiMessage: UIMessage)
}