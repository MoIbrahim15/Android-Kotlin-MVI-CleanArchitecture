package com.mi.mvi.presentation.auth


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.mi.mvi.R
import com.mi.mvi.presentation.base.BaseActivity
import com.mi.mvi.presentation.auth.state.AuthEventState
import com.mi.mvi.presentation.main.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

@ExperimentalCoroutinesApi
class AuthActivity : BaseActivity(R.layout.activity_auth) {

    private val authViewModel: AuthViewModel by currentScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
        checkPreviousAuthUser()
    }

    private fun subscribeObservers() {
        authViewModel.dataState.observe(this, Observer { dataState ->
            onDataStateChangeListener(dataState)
            dataState.data?.let { viewState ->
                viewState.authTokenEntity?.let { authToken ->
                    authViewModel.setAuthToken(authToken)
                }
            }
        })

        authViewModel.viewState.observe(this, Observer {
            it.authTokenEntity?.let { authToken ->
                sessionManager.login(authToken)
            }
        })

        sessionManager.cachedTokenEntity.observe(
            this,
            Observer { authToken ->
                authToken?.let {
                    if (it.account_pk != -1 && it.token != null) {
                        navMainActivity()
                    }
                }
            })
    }


    private fun checkPreviousAuthUser() {
        authViewModel.setEventState(AuthEventState.CheckTokenEvent())

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