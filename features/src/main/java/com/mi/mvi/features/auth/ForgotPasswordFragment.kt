package com.mi.mvi.features.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.domain.Constants.Companion.PASSWORD_RESET_URL
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.datastate.MessageType
import com.mi.mvi.domain.datastate.StateMessage
import com.mi.mvi.domain.datastate.UIComponentType
import com.mi.mvi.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_forget_password.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class ForgetPasswordFragment : BaseFragment(R.layout.fragment_forget_password) {

    private val webInteractionCallback: WebAppInterface.OnWebInteractionCallback =
        object : WebAppInterface.OnWebInteractionCallback {
            override fun onSuccess(email: String) {
                GlobalScope.launch(Main) {
                    onPasswordResetSuccessfully()
                }
            }

            override fun onError(errorMessage: String) {
                GlobalScope.launch(Main) {
                    dataStateChangeListener?.onDataStateChangeListener(
                        dataState =  DataState.ERROR<Any>(
                            stateMessage =  StateMessage(
                                errorMessage,
                                 UIComponentType.DIALOG,
                                 MessageType.ERROR
                            )
                        )
                    )
                }
            }

            override fun onLoading(isLoading: Boolean) {
                GlobalScope.launch(Main) {
                    dataStateChangeListener?.onDataStateChangeListener(
                         DataState.LOADING(isLoading, cachedData = null)
                    )
                }
            }
        }

    private fun onPasswordResetSuccessfully() {
        parent_view.removeView(webview)
        webview.destroy()
        val animation = TranslateAnimation(
            password_reset_done_container.width.toFloat(),
            0F, 0F, 0F
        )
        animation.duration = 500
        password_reset_done_container.startAnimation(animation)
        password_reset_done_container.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPasswordResetView()
        return_to_launcher_fragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPasswordResetView() {
        dataStateChangeListener?.onDataStateChangeListener(
             DataState.LOADING(true, cachedData = null)
        )

        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                dataStateChangeListener?.onDataStateChangeListener(
                     DataState.LOADING(
                        false,
                        cachedData = null
                    )
                )
            }
        }
        webview.loadUrl(PASSWORD_RESET_URL)
        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(
            WebAppInterface(webInteractionCallback),
            "AndroidTextListener"
        )
    }

    class WebAppInterface
    constructor(
        private val callback: OnWebInteractionCallback
    ) {

        @JavascriptInterface
        fun onSuccess(email: String) {
            callback.onSuccess(email)
        }

        @JavascriptInterface
        fun onError(errorMessage: String) {
            callback.onError(errorMessage)
        }

        @JavascriptInterface
        fun onLoading(isLoading: Boolean) {
            callback.onLoading(isLoading)
        }

        interface OnWebInteractionCallback {
            fun onSuccess(email: String)
            fun onError(errorMessage: String)
            fun onLoading(isLoading: Boolean)
        }
    }
}
