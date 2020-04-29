package com.mi.mvi.remote.koin

import com.mi.mvi.data.datasource.remote.AccountRemoteDataSource
import com.mi.mvi.data.datasource.remote.AuthRemoteDataSource
import com.mi.mvi.data.datasource.remote.BlogRemoteDataSource
import com.mi.mvi.remote.mapper.BaseEntityMapper
import com.mi.mvi.remote.mapper.BlogPostEntityMapper
import com.mi.mvi.remote.mapper.BlogPostListEntityMapper
import com.mi.mvi.remote.mapper.UserEntityMapper
import com.mi.mvi.remote.service.AccountAPIService
import com.mi.mvi.remote.service.AuthAPIService
import com.mi.mvi.remote.service.BlogAPIService
import com.mi.mvi.remote.source.AccountRemoteDataSourceImpl
import com.mi.mvi.remote.source.AuthRemoteDataSourceImpl
import com.mi.mvi.remote.source.BlogRemoteDataSourceImpl
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://open-api.xyz/api/"

val remoteModule = module {

    single {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(6, TimeUnit.SECONDS)
        httpClient.writeTimeout(6, TimeUnit.SECONDS)
        httpClient.connectTimeout(6, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    factory { BaseEntityMapper() }
    factory { UserEntityMapper() }
    factory { BlogPostEntityMapper() }
    factory { BlogPostListEntityMapper() }

    factory { provideAuthAPI(get()) }
    factory { provideAccountAPI(get()) }
    factory { provideBlogAPI(get()) }

    factory<AuthRemoteDataSource> {
        AuthRemoteDataSourceImpl(
            get(),
            get()
        )
    }

    factory<AccountRemoteDataSource> {
        AccountRemoteDataSourceImpl(
            get(),
            get(),
            get()
        )
    }
    factory<BlogRemoteDataSource> {
        BlogRemoteDataSourceImpl(
            get(), get(), get(), get()
        )
    }
}

fun provideAuthAPI(retrofit: Retrofit): AuthAPIService =
    retrofit.create(AuthAPIService::class.java)

fun provideAccountAPI(retrofit: Retrofit): AccountAPIService =
    retrofit.create(AccountAPIService::class.java)

fun provideBlogAPI(retrofit: Retrofit): BlogAPIService =
    retrofit.create(BlogAPIService::class.java)
