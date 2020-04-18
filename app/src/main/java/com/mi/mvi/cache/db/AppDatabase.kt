package com.mi.mvi.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mi.mvi.cache.entity.AuthTokenEntity
import com.mi.mvi.cache.entity.BlogPostEntity
import com.mi.mvi.cache.entity.UserEntity

@Database(entities = [AuthTokenEntity::class, UserEntity::class, BlogPostEntity::class], version = 1)
abstract class AppDatabase :RoomDatabase(){

    abstract fun getAuthTokenDao() : AuthTokenDao
    abstract fun getAccountDao() : AccountDao
    abstract fun getBlogPostDao(): BlogPostDao

    companion object{
        const val DATABASE_NAME = "app_db"
    }
}