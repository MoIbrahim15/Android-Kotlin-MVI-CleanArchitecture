package com.mi.mvi.ui.auth


import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mi.mvi.R
import com.mi.mvi.base.BaseActivity
import com.mi.mvi.ui.main.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        authViewModel.viewState.observe(this, Observer {
            it.authToken?.let { authToken ->
                sessionManager.login(authToken)
            }
        })
        sessionManager.cachedToken.observe(
            this,
            Observer { authToken ->
                authToken?.let {
                    if (it.account_pk != -1 || it.token != null) {
                        navMainActivity()
                        finish()
                    }
                }
            })
    }

    private fun navMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_auth
    }
}