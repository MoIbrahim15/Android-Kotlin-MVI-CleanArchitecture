package com.mi.mvi.koin

import com.mi.mvi.data.response_handler.ErrorHandler
import com.mi.mvi.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {

    factory { ErrorHandler() }

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
}