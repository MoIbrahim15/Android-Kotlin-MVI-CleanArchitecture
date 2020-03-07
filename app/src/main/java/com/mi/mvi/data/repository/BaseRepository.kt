package com.mi.mvi.data.repository

import androidx.lifecycle.LiveData
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.response_handler.Response
import com.mi.mvi.data.response_handler.ResponseEntity
import com.mi.mvi.data.response_handler.ResponseView
import com.mi.mvi.ui.auth.state.AuthViewState

abstract class BaseRepository {

    protected fun returnErrorResponse(
        responseEntity: ResponseEntity,
        responseView: ResponseView
    ): LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.ERROR(Response(responseEntity, responseView))
            }
        }
    }

}