package com.mi.mvi.presentation

import com.mi.mvi.utils.response_handler.DataState

interface DataStateChangeListener {
    fun onDataStateChangeListener(dataState: DataState<*>?)
    fun hideSoftKeyboard()
    fun isStoragePermissionGranted() : Boolean
}