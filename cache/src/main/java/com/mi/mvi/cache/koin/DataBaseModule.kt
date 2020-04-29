package com.mi.mvi.cache.koin

import androidx.room.Room
import com.mi.mvi.cache.db.AppDatabase
import com.mi.mvi.cache.mapper.BlogPostEntityMapper
import com.mi.mvi.cache.mapper.TokenEntityMapper
import com.mi.mvi.cache.mapper.UserEntityMapper
import com.mi.mvi.cache.source.AccountCacheDataSourceImpl
import com.mi.mvi.cache.source.BlogCacheDataSourceImpl
import com.mi.mvi.cache.source.TokenCacheDataSourceImpl
import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.cache.TokenCacheDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }
    single { get<AppDatabase>().getAuthTokenDao() }
    single { get<AppDatabase>().getAccountDao() }
    single { get<AppDatabase>().getBlogPostDao() }

    factory { TokenEntityMapper() }
    factory { UserEntityMapper() }
    factory { BlogPostEntityMapper() }

    factory<TokenCacheDataSource> {
        TokenCacheDataSourceImpl(
            get(),
            get()
        )
    }
    factory<AccountCacheDataSource> {
        AccountCacheDataSourceImpl(
            get(),
            get(),
            get(),
            get()
        )
    }

    factory<BlogCacheDataSource> {
        BlogCacheDataSourceImpl(
            get(),
            get(),
            get(),
            get()
        )
    }
}
