package com.mi.mvi.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mi.mvi.cache.model.CachedToken

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cachedToken: CachedToken): Long

    @Query("UPDATE auth_token SET token = null WHERE account_pk = :pk")
    suspend fun nullifyToken(pk: Int)

    @Query("SELECT * FROM auth_token WHERE account_pk = :pk")
    suspend fun searchTokenByPk(pk: Int): CachedToken?
}
