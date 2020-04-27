package com.mi.mvi.features.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.mi.mvi.R
import com.mi.mvi.features.base.BaseActivity
import com.mi.mvi.features.main.MainActivity
import com.mi.mvi.mapper.TokenMapper
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class AuthActivity : BaseActivity(R.layout.activity_auth) {

    private val authViewModel: AuthViewModel by viewModel()
    private val tokenMapper: TokenMapper = TokenMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        authViewModel.dataState.observe(this, Observer { dataState ->
            onDataStateChangeListener(dataState)
            dataState.data?.let { viewState ->
                viewState.token?.let { authToken ->
                    authViewModel.setAuthToken(authToken)
                }
            }
        })

        authViewModel.viewState.observe(this, Observer {
            it.token?.let { authToken ->
                sessionManager.login(tokenMapper.mapToView(authToken))
            }
        })

        sessionManager.cachedTokenViewEntity.observe(
            this,
            Observer { authToken ->
                authToken?.let {
                    if (it.account_pk != -1 && it.token != null) {
                        navMainActivity()
                    }
                }
            })
    }

    private fun navMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
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
