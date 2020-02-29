package com.mi.mvi.koin

import com.mi.mvi.data.network.auth.AuthApiService
import com.mi.mvi.data.repository.auth.AuthRepository
import com.mi.mvi.domain.auth.ForgetUseCase
import com.mi.mvi.domain.auth.LoginUseCase
import com.mi.mvi.domain.auth.RegisterUseCase
import com.mi.mvi.ui.auth.AuthViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    factory { provideAPIService(get()) }
    factory { AuthRepository(get(), get(), get(), get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { ForgetUseCase(get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
}


fun provideAPIService(retrofit: Retrofit): AuthApiService =
    retrofit.create(AuthApiService::class.java)

