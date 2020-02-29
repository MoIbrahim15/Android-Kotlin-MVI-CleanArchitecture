package com.mi.mvi.koin

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mi.mvi.R
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val glideModule = module {
    single {
        RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)
    }
    single {
        Glide.with(androidContext())
            .applyDefaultRequestOptions(get())
    }
}