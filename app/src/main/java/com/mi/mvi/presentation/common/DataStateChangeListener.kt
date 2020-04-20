package com.mi.mvi.presentation.common

import com.mi.mvi.utils.response_handler.DataState

interface DataStateChangeListener {
    fun onDataStateChangeListener(dataState: DataState<*>?)
}
