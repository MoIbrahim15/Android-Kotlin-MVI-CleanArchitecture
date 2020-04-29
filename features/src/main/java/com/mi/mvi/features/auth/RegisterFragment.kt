package com.mi.mvi.features.auth

import android.os.Bundle
import android.view.View
import com.mi.mvi.R
import com.mi.mvi.base.BaseFragment
import com.mi.mvi.events.AuthEventState
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
    }

    private fun register() {
        viewModel.setEventState(
            AuthEventState.RegisterEvent(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }
}
