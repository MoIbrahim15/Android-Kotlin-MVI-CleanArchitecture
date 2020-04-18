package com.mi.mvi.koin

import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.cache.AuthCacheDataSource
import com.mi.mvi.data.datasource.remote.AuthRemoteDataSource
import com.mi.mvi.data.repository.AuthRepositoryImpl
import com.mi.mvi.cache.source.AccountCacheDataSourceImpl
import com.mi.mvi.cache.source.AuthCacheDataSourceImpl
import com.mi.mvi.remote.service.AuthAPIService
import com.mi.mvi.remote.source.AuthRemoteDataSourceImpl
import com.mi.mvi.domain.repository.AuthRepository
import com.mi.mvi.domain.usecase.auth.CheckTokenUseCase
import com.mi.mvi.domain.usecase.auth.LoginUseCase
import com.mi.mvi.domain.usecase.auth.RegisterUseCase
import com.mi.mvi.presentation.auth.AuthActivity
import com.mi.mvi.presentation.auth.AuthViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val authModule = module {
    scope(named<AuthActivity>()) {
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


fun provideAuthAPI(retrofit: Retrofit): AuthAPIService =
    retrofit.create(AuthAPIService::class.java)

