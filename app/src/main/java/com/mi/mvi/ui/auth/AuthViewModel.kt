package com.mi.mvi.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mi.mvi.domain.auth.ForgetUseCase
import com.mi.mvi.domain.auth.LoginUseCase
import com.mi.mvi.domain.auth.RegisterUseCase

class AuthViewModel(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val forgetUseCase: ForgetUseCase
) : ViewModel() {
}