package com.mi.mvi.cache.koin

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val APP_PREFERENCES: String = "com.mi.mvi.APP_PREFERENCES"

val sharedPrefsModule = module {
    single {
        androidContext().getSharedPreferences(
            APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
    single { (get() as SharedPreferences).edit() }
}
