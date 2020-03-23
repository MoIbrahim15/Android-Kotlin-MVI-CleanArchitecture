package com.mi.mvi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mi.mvi.data.models.AccountProperties
import com.mi.mvi.data.models.AuthToken

@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class AppDatabase :RoomDatabase(){

    abstract fun getAuthTokenDao() :AuthTokenDao
    abstract fun getAccountDao() : AccountDao

    companion object{
        const val DATABASE_NAME = "app_db"
    }
}