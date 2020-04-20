package com.mi.mvi.data.datasource.cache

import com.mi.mvi.cache.entity.AuthTokenEntity

interface AuthCacheDataSource {

    suspend fun insert(authTokenEntity: AuthTokenEntity): Long

    suspend fun nullifyToken(pk: Int)

    suspend fun searchTokenByPk(pk: Int): AuthTokenEntity?
}
