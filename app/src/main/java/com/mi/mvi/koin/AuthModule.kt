package com.mi.mvi.koin

import com.mi.mvi.data.network.auth.AuthApiService
import com.mi.mvi.data.repository.auth.AuthRepository
import com.mi.mvi.domain.auth.CheckTokenUseCase
import com.mi.mvi.domain.auth.LoginUseCase
import com.mi.mvi.domain.auth.RegisterUseCase
import com.mi.mvi.ui.auth.AuthViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val authModule = module {
    factory { provideAPIService(get()) }
    factory { AuthRepository(get(), get(), get(), get(), get(), get(), get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { CheckTokenUseCase(get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
}


fun provideAPIService(retrofit: Retrofit): AuthApiService =
    retrofit.create(AuthApiService::class.java)

