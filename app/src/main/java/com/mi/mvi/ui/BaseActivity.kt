package com.mi.mvi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.response_handler.Response
import com.mi.mvi.data.response_handler.ResponseEntity
import com.mi.mvi.data.response_handler.ResponseView
import com.mi.mvi.data.session.SessionManager
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity(),
    DataStateChangeListener {

    val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
    }

    override fun onDataStateChangeListener(dataState: DataState<*>?) {
        dataState?.let {
            displayLoading(it.loading.isLoading)
            it.error?.getContentIfNotHandled()?.let { error ->
                handleResponseState(error.response)
            }
            it.data?.response?.getContentIfNotHandled().let {response ->
                handleResponseState(response)
            }
        }
    }

    private fun handleResponseState(response: Response?) {
        response?.responseEntity?.let {
            val message = getErrorMessage(it)
            when (response.responseView) {
                is ResponseView.NONE -> {

                }
                is ResponseView.DIALOG -> {
                    displayErrorDialog(message)
                }
                is ResponseView.TOAST -> {
                    displayToast(message)
                }
            }
        }
    }

    private fun getErrorMessage(responseEntity: ResponseEntity?): String {
        return ""
    }

    abstract fun getLayoutRes(): Int
    abstract fun displayLoading(isLoading: Boolean)
}