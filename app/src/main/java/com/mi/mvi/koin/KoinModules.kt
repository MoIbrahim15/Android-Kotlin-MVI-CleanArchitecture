package com.mi.mvi.koin

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
val koinModules = listOf(
    databaseModule,
    sharedPrefsModule,
    networkModule,
    glideModule,
    authModule,
    mainModule
)