package com.mi.mvi.koin

import com.mi.mvi.features.auth.AuthViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val authModule = module {
    viewModel { AuthViewModel(get(), get(), get(), get()) }
}
