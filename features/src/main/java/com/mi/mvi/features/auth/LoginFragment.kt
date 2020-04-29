package com.mi.mvi.features.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.base.BaseFragment
import com.mi.mvi.events.AuthEventState
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@ExperimentalCoroutinesApi
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewModel: AuthViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnForget.setOnClickListener {
            navForgetPassword()
        }
        btnRegister.setOnClickListener { navRegistration() }

        btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        viewModel.setEventState(
            AuthEventState.LoginEvent(
                input_email.text.toString(),
                input_password.text.toString()
            )
        )
    }

    private fun navRegistration() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun navForgetPassword() {
        findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
    }
}
