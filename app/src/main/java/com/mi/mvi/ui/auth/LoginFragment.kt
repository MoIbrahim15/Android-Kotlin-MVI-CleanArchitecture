package com.mi.mvi.ui.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.ui.BaseFragment
import com.mi.mvi.ui.auth.state.AuthEventState
import com.mi.mvi.ui.auth.state.LoginFields
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseFragment() {

    private val viewModel: AuthViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnForget.setOnClickListener {
            navForgetPassword()
        }
        btnRegister.setOnClickListener { navRegistration() }


        btnLogin.setOnClickListener { login() }

        subscribeObservers()
    }

    private fun login() {
        viewModel.setStateEvent(
            AuthEventState.LoginEvent(
                input_email.text.toString(),
                input_password.text.toString()
            )
        )
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.loginFields?.let { loginFields ->
                loginFields.email?.let { email -> input_email.setText(email) }
                loginFields.password?.let { password -> input_password.setText(password) }
            }
        })
    }

    private fun navRegistration() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun navForgetPassword() {
        findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(
            LoginFields(
                input_email.text.toString(),
                input_password.text.toString()
            )
        )
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_login
    }
}