package com.mi.mvi.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mi.mvi.R
import com.mi.mvi.ui.auth.state.LoginFields
import com.mi.mvi.ui.auth.state.RegistrationFields
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.input_email
import kotlinx.android.synthetic.main.fragment_login.input_password
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class RegisterFragment : Fragment() {

    private val viewModel: AuthViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("registerFragment", viewModel.hashCode().toString())

        subscribeObservers()
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