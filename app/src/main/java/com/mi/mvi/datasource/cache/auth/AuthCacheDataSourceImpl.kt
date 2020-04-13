package com.mi.mvi.datasource.cache.auth

import com.mi.mvi.data.datasource.cache.AuthCacheDataSource
import com.mi.mvi.datasource.model.AuthToken

class AuthCacheDataSourceImpl(
    private val authTokenDao: AuthTokenDao
) : AuthCacheDataSource {

    override suspend fun insert(authToken: AuthToken): Long {
        return authTokenDao.insert(authToken)
    }

    override suspend fun nullifyToken(pk: Int) {
        return authTokenDao.nullifyToken(pk)
    }

    override suspend fun searchTokenByPk(pk: Int): AuthToken? {
        return authTokenDao.searchTokenByPk(pk)
    }
}