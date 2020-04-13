package com.mi.mvi.koin

import android.content.Context
import android.content.SharedPreferences
import com.mi.mvi.utils.SharedPreferenceKeys
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPrefsModule = module {
    single {
        androidContext().getSharedPreferences(
            SharedPreferenceKeys.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
    single { (get() as SharedPreferences).edit() }
}