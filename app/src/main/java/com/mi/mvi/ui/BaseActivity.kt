package com.mi.mvi.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.data.response_handler.Response
import com.mi.mvi.data.response_handler.ResponseView
import com.mi.mvi.data.session.SessionManager
import org.koin.android.ext.android.inject

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId),
    DataStateChangeListener {

    val sessionManager: SessionManager by inject()

    override fun onDataStateChangeListener(dataState: DataState<*>?) {
        dataState?.let {
            displayLoading(it.loading.isLoading)
            it.error?.getContentIfNotHandled()?.let { error ->
                handleResponseState(error.response)
            }
            it.data?.response?.getContentIfNotHandled().let { response ->
                handleResponseState(response)
            }
        }
    }

    private fun handleResponseState(response: Response?) {
        response?.messageRes?.let { messageRes ->
            val message = getString(messageRes)
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

    override fun hideSoftKeyboard() {
        currentFocus?.let {   currentFocus ->
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }

    }

    abstract fun displayLoading(isLoading: Boolean)
}