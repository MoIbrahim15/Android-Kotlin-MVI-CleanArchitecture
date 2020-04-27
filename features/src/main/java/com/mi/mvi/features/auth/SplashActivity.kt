package com.mi.mvi.features.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.mi.mvi.R
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.eventstate.AuthEventState
import com.mi.mvi.features.base.BaseActivity
import com.mi.mvi.features.main.MainActivity
import com.mi.mvi.mapper.TokenMapper
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class SplashActivity : BaseActivity(R.layout.activity_splash) {

    private val authViewModel: AuthViewModel by viewModel()
    private val tokenMapper = TokenMapper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
        checkPreviousAuthUser()
    }

    private fun subscribeObservers() {
        authViewModel.dataState.observe(this, Observer { dataState ->
            onDataStateChangeListener(dataState)
            when (dataState) {
                is DataState.SUCCESS -> {
                    dataState.data?.token?.let { token ->
                        sessionManager.login(tokenMapper.mapToView(token))
                    } ?: navLoginActivity()
                }
                is DataState.ERROR -> {
                    navLoginActivity()
                }
            }
        })

        sessionManager.cachedTokenViewEntity.observe(
            this,
            Observer { authToken ->
                authToken?.let {
                    if (it.account_pk != -1 && it.token != null) {
                        navMainActivity()
                    } else {
                        navLoginActivity()
                    }
                }
            })
    }

    private fun checkPreviousAuthUser() {
        authViewModel.setEventState(AuthEventState.CheckTokenEvent)
    }

    private fun navMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navLoginActivity() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    override fun displayLoading(isLoading: Boolean) {
        progress_bar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
