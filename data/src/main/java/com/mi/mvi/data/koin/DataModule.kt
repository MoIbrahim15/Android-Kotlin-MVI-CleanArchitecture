package com.mi.mvi.data.koin

import com.mi.mvi.data.mapper.BlogPostMapper
import com.mi.mvi.data.mapper.TokenMapper
import com.mi.mvi.data.mapper.UserMapper
import com.mi.mvi.data.repository.AccountRepositoryImpl
import com.mi.mvi.data.repository.AuthRepositoryImpl
import com.mi.mvi.data.repository.BlogRepositoryImpl
import com.mi.mvi.data.repository.CreateBlogRepositoryImpl
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.domain.repository.AuthRepository
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.domain.repository.CreateBlogRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val dataModule = module {

    factory { TokenMapper() }
    factory { BlogPostMapper() }
    factory { UserMapper() }

    factory<AuthRepository> {
        AuthRepositoryImpl(
            get(),
            get(),
            get(),
            get()
        )
    }
    factory<AccountRepository> {
        AccountRepositoryImpl(
            get(),
            get(),
            get()
        )
    }
    factory<BlogRepository> {
        BlogRepositoryImpl(
            get(),
            get(),
            get()
        )
    }
    factory<CreateBlogRepository> {
        CreateBlogRepositoryImpl(
            get(),
            get()
        )
    }
}
