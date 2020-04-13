package com.mi.mvi.datasource.cache.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mi.mvi.datasource.model.AuthToken

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(authToken: AuthToken): Long

    @Query("UPDATE auth_token SET token = null WHERE account_pk = :pk")
    suspend fun nullifyToken(pk: Int)

    @Query("SELECT * FROM auth_token WHERE account_pk = :pk")
    suspend fun searchTokenByPk(pk: Int): AuthToken?

}