package com.mi.mvi.common

import com.mi.mvi.domain.datastate.DataState

interface DataStateChangeListener {
    fun onDataStateChangeListener(dataState: DataState<*>?)
}
