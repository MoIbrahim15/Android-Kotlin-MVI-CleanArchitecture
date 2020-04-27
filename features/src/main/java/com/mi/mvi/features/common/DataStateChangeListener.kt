package com.mi.mvi.features.common

import com.mi.mvi.domain.datastate.DataState

interface DataStateChangeListener {
    fun onDataStateChangeListener(dataState: DataState<*>?)
}
