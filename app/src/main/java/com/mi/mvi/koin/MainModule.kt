package com.mi.mvi.koin

import com.mi.mvi.data.network.main.MainApiService
import com.mi.mvi.data.repository.main.AccountRepository
import com.mi.mvi.domain.main.account.ChangePasswordUseCase
import com.mi.mvi.domain.main.account.GetAccountUseCase
import com.mi.mvi.domain.main.account.UpdateAccountUseCase
import com.mi.mvi.ui.main.MainActivity
import com.mi.mvi.ui.main.account.AccountViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val mainModule = module {
    scope(named<MainActivity>()) {
        //Account Scope
        factory { provideAccountApi(get()) }
        factory { AccountRepository(get(), get(), get(), get()) }
        factory { GetAccountUseCase(get()) }
        factory { UpdateAccountUseCase(get()) }
        factory { ChangePasswordUseCase(get()) }
        viewModel { AccountViewModel(get(), get(), get(), get()) }


    }
}

fun provideAccountApi(retrofit: Retrofit): MainApiService =
    retrofit.create(MainApiService::class.java)
