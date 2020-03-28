package com.mi.mvi.ui

import com.mi.mvi.data.response_handler.DataState

interface DataStateChangeListener {
    fun onDataStateChangeListener(dataState: DataState<*>?)
    fun hideSoftKeyboard()
}