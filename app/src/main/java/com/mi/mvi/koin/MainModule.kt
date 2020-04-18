package com.mi.mvi.koin

import com.mi.mvi.cache.source.AccountCacheDataSourceImpl
import com.mi.mvi.cache.source.BlogCacheDataSourceImpl
import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.remote.AccountRemoteDataSource
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.data.repository.AccountRepositoryImpl
import com.mi.mvi.data.repository.BlogRepositoryImpl
import com.mi.mvi.data.repository.CreateBlogRepositoryImpl
import com.mi.mvi.domain.repository.AccountRepository
import com.mi.mvi.domain.repository.BlogRepository
import com.mi.mvi.domain.repository.CreateBlogRepository
import com.mi.mvi.domain.usecase.account.ChangePasswordUseCase
import com.mi.mvi.domain.usecase.account.GetAccountUseCase
import com.mi.mvi.domain.usecase.account.UpdateAccountUseCase
import com.mi.mvi.domain.usecase.blogs.*
import com.mi.mvi.presentation.main.MainActivity
import com.mi.mvi.presentation.main.account.AccountViewModel
import com.mi.mvi.presentation.main.blog.viewmodel.BlogViewModel
import com.mi.mvi.presentation.main.create_blog.CreateBlogViewModel
import com.mi.mvi.remote.service.AccountAPIService
import com.mi.mvi.remote.service.BlogAPIService
import com.mi.mvi.remote.source.AccountRemoteDataSourceImpl
import com.mi.mvi.remote.source.BlogRemoteDataSourceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val mainModule = module {
    scope(named<MainActivity>()) {

        //Account Scope
        factory { provideAccountAPI(get()) }
        factory<AccountRemoteDataSource> {
            AccountRemoteDataSourceImpl(
                get()
            )
        }
        factory<AccountCacheDataSource> {
            AccountCacheDataSourceImpl(
                get()
            )
        }
        factory<AccountRepository> {
            AccountRepositoryImpl(
                get(),
                get()
            )
        }
        factory { GetAccountUseCase(get()) }
        factory { UpdateAccountUseCase(get()) }
        factory { ChangePasswordUseCase(get()) }
        viewModel { AccountViewModel(get(), get(), get(), get()) }


        //Blogs Scope
        factory { provideBlogAPI(get()) }
        factory<BlogRemoteDataSource> {
            BlogRemoteDataSourceImpl(
                get()
            )
        }
        factory<BlogCacheDataSource> {
            BlogCacheDataSourceImpl(
                get()
            )
        }
        factory<BlogRepository> {
            BlogRepositoryImpl(
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
        factory { SearchBlogUseCase(get()) }
        factory { IsAuthorBlogPostUseCase(get()) }
        factory { DeleteBlogPostUseCase(get()) }
        factory { UpdateBlogPostUseCase(get()) }
        factory { CreateBlogUseCase(get()) }
        viewModel {
            BlogViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get())
        }
        viewModel {
            CreateBlogViewModel(
                get(),
                get()
            )
        }
    }
}

fun provideAccountAPI(retrofit: Retrofit): AccountAPIService =
    retrofit.create(AccountAPIService::class.java)

fun provideBlogAPI(retrofit: Retrofit): BlogAPIService =
    retrofit.create(BlogAPIService::class.java)