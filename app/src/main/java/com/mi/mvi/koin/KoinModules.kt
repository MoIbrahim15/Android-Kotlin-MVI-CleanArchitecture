package com.mi.mvi.koin

import com.mi.mvi.cache.koin.databaseModule
import com.mi.mvi.cache.koin.sharedPrefsModule
import com.mi.mvi.data.koin.dataModule
import com.mi.mvi.domain.koin.useCaseModule
import com.mi.mvi.remote.koin.remoteModule

val koinModules = listOf(
    databaseModule,
    sharedPrefsModule,
    remoteModule,
    dataModule,
    useCaseModule,
    authModule,
    mainModule
)
