package com.mi.mvi.data.datasource.cache

import com.mi.mvi.datasource.model.AuthToken

interface AuthCacheDataSource {

    suspend fun insert(authToken: AuthToken): Long

    suspend fun nullifyToken(pk: Int)

    suspend fun searchTokenByPk(pk: Int): AuthToken?
}