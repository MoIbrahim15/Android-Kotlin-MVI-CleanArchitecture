package com.mi.mvi.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    private val authViewModel: AuthViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("loginFragment", authViewModel.hashCode().toString())

        btnForget.setOnClickListener {
            navForgetPassword()
        }
        btnRegister.setOnClickListener { navRegistration() }

        btnLogin.setOnClickListener {
            authViewModel.login(input_email.text.toString(), input_password.text.toString())
                .observe(this, Observer {
                    Log.v("NetworkResponse", it.toString())
                })
        }
    }

    private fun navRegistration() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun navForgetPassword() {
        findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
    }
}