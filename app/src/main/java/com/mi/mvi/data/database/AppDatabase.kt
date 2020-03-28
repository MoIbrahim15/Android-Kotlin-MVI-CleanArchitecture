package com.mi.mvi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mi.mvi.data.models.AccountProperties
import com.mi.mvi.data.models.AuthToken
import com.mi.mvi.data.models.BlogPost

@Database(entities = [AuthToken::class, AccountProperties::class, BlogPost::class], version = 1)
abstract class AppDatabase :RoomDatabase(){

    abstract fun getAuthTokenDao() :AuthTokenDao
    abstract fun getAccountDao() : AccountDao
    abstract fun getBlogPostDao(): BlogPostDao

    companion object{
        const val DATABASE_NAME = "app_db"
    }
}