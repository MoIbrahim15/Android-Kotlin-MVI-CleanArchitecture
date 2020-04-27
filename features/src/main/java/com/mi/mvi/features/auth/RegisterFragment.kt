package com.mi.mvi.features.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.mi.mvi.R
import com.mi.mvi.domain.viewstate.RegistrationFields
import com.mi.mvi.features.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@ExperimentalCoroutinesApi
class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    private val viewModel: AuthViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister.setOnClickListener { register() }
        subscribeObservers()
    }

    private fun register() {
        viewModel.setEventState(
            com.mi.mvi.eventstate.AuthEventState.RegisterEvent(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.registrationFields?.let { loginFields ->
                loginFields.email?.let { email -> input_email.setText(email) }
                loginFields.username?.let { username -> input_username.setText(username) }
                loginFields.password?.let { password -> input_password.setText(password) }
                loginFields.confirmPassword?.let { confirmPassword ->
                    input_password_confirm.setText(
                        confirmPassword
                    )
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }
}
