package com.mi.mvi.koin

import com.mi.mvi.cache.source.AccountCacheDataSourceImpl
import com.mi.mvi.cache.source.AuthCacheDataSourceImpl
import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.cache.AuthCacheDataSource
import com.mi.mvi.data.datasource.remote.AuthRemoteDataSource
import com.mi.mvi.data.repository.AuthRepositoryImpl
import com.mi.mvi.domain.repository.AuthRepository
import com.mi.mvi.domain.usecase.auth.CheckTokenUseCase
import com.mi.mvi.domain.usecase.auth.LoginUseCase
import com.mi.mvi.domain.usecase.auth.RegisterUseCase
import com.mi.mvi.presentation.auth.AuthViewModel
import com.mi.mvi.presentation.auth.SplashActivity
import com.mi.mvi.remote.source.AuthRemoteDataSourceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


@ExperimentalCoroutinesApi
val sessionModule = module {
    scope(named<SplashActivity>()) {
        factory { provideAuthAPI(get()) }
        factory<AuthRemoteDataSource> {
            AuthRemoteDataSourceImpl(
                get()
            )
        }
        factory<AuthCacheDataSource> {
            AuthCacheDataSourceImpl(
                get()
            )
        }
        factory<AccountCacheDataSource> {
            AccountCacheDataSourceImpl(
                get()
            )
        }
        factory<AuthRepository> {
            AuthRepositoryImpl(
                get(),
                get(),
                get(),
                get(),
                get()
            )
        }
        factory { LoginUseCase(get()) }
        factory { RegisterUseCase(get()) }
        factory { CheckTokenUseCase(get()) }
        viewModel { AuthViewModel(get(), get(), get()) }
    }
}
