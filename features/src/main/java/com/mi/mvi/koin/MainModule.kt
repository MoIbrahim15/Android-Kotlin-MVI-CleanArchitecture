package com.mi.mvi.koin

import com.mi.mvi.common.SessionManager
import com.mi.mvi.features.main.account.AccountViewModel
import com.mi.mvi.features.main.blog.viewmodel.BlogViewModel
import com.mi.mvi.features.main.create_blog.CreateBlogViewModel
import com.mi.mvi.mapper.BlogPostMapper
import com.mi.mvi.mapper.TokenMapper
import com.mi.mvi.mapper.UserMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val mainModule = module {
    single { SessionManager(get(), androidContext()) }

    factory { TokenMapper() }
    factory { UserMapper() }
    factory { BlogPostMapper() }
    viewModel { AccountViewModel(get(), get(), get(), get(), get(), get()) }

    viewModel {
        BlogViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel {
        CreateBlogViewModel(
            get(),
            get(),
            get()
        )
    }
}
