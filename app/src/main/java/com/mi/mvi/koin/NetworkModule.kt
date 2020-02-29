package com.mi.mvi.koin

import org.koin.dsl.module
import retrofit2.Retrofit


const val BASE_URL = "https://www.google.com"
val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
    }
}