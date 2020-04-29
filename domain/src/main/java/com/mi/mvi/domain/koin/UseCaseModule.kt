package com.mi.mvi.domain.koin

import com.mi.mvi.domain.usecase.account.ChangePasswordUseCase
import com.mi.mvi.domain.usecase.account.GetAccountUseCase
import com.mi.mvi.domain.usecase.account.UpdateAccountUseCase
import com.mi.mvi.domain.usecase.auth.CheckTokenUseCase
import com.mi.mvi.domain.usecase.auth.LoginUseCase
import com.mi.mvi.domain.usecase.auth.RegisterUseCase
import com.mi.mvi.domain.usecase.blogs.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { CheckTokenUseCase(get()) }
    factory { GetAccountUseCase(get()) }
    factory { UpdateAccountUseCase(get()) }
    factory { ChangePasswordUseCase(get()) }
    factory { SearchBlogUseCase(get()) }
    factory { IsAuthorBlogPostUseCase(get()) }
    factory { DeleteBlogPostUseCase(get()) }
    factory { UpdateBlogPostUseCase(get()) }
    factory { CreateBlogUseCase(get()) }
    factory { FiltrationUseCase(get()) }
}
