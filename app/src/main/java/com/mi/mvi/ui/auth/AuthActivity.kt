package com.mi.mvi.ui.auth


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.mi.mvi.R
import com.mi.mvi.ui.BaseActivity
import com.mi.mvi.ui.auth.state.AuthEventState
import com.mi.mvi.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class AuthActivity : BaseActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
        checkPreviousAuthUser()
    }

    private fun subscribeObservers() {
        authViewModel.dataState.observe(this, Observer { dataState ->
            onDataStateChangeListener(dataState)
            dataState.data?.let {
                it.data?.getContentIfNotHandled()?.let { viewState ->
                    viewState.authToken?.let { authToken ->
                        authViewModel.setAuthToken(authToken)
                    }
                }
            }
        })

        authViewModel.viewState.observe(this, Observer {
            it.authToken?.let { authToken ->
                sessionManager.login(authToken)
            }
        })

        sessionManager.cachedToken.observe(
            this,
            Observer { authToken ->
                authToken?.let {
                    if (it.account_pk != -1 && it.token != null) {
                        navMainActivity()
                    }
                }
            })
    }


    private fun checkPreviousAuthUser(){
        authViewModel.setStateEvent(AuthEventState.CheckTokenEvent())

    }
    private fun navMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_auth
    }

    override fun displayLoading(isLoading: Boolean) {
        progress_bar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}