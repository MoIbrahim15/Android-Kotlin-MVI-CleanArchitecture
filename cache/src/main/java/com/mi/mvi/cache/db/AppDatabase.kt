package com.mi.mvi.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mi.mvi.cache.model.CachedBlogPost
import com.mi.mvi.cache.model.CachedToken
import com.mi.mvi.cache.model.CachedUser

@Database(entities = [CachedUser::class, CachedToken::class, CachedBlogPost::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    //TODO add shared preference to cache module
    abstract fun getAuthTokenDao(): AuthTokenDao
    abstract fun getAccountDao(): AccountDao
    abstract fun getBlogPostDao(): BlogPostDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}
