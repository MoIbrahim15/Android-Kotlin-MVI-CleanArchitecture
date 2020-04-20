package com.mi.mvi.presentation.common

import com.mi.mvi.utils.DataState

interface DataStateChangeListener {
    fun onDataStateChangeListener(dataState: DataState<*>?)
}
