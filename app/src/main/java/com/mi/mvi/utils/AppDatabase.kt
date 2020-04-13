package com.mi.mvi.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mi.mvi.datasource.cache.account.AccountDao
import com.mi.mvi.datasource.cache.auth.AuthTokenDao
import com.mi.mvi.datasource.cache.blog.BlogPostDao
import com.mi.mvi.datasource.model.AccountProperties
import com.mi.mvi.datasource.model.AuthToken
import com.mi.mvi.datasource.model.BlogPost

@Database(entities = [AuthToken::class, AccountProperties::class, BlogPost::class], version = 1)
abstract class AppDatabase :RoomDatabase(){

    abstract fun getAuthTokenDao() : AuthTokenDao
    abstract fun getAccountDao() : AccountDao
    abstract fun getBlogPostDao(): BlogPostDao

    companion object{
        const val DATABASE_NAME = "app_db"
    }
}