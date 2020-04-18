package com.mi.mvi.cache.source

import com.mi.mvi.cache.db.AuthTokenDao
import com.mi.mvi.data.datasource.cache.AuthCacheDataSource
import com.mi.mvi.cache.entity.AuthTokenEntity

class AuthCacheDataSourceImpl(
    private val authTokenDao: AuthTokenDao
) : AuthCacheDataSource {

    override suspend fun insert(authTokenEntity: AuthTokenEntity): Long {
        return authTokenDao.insert(authTokenEntity)
    }

    override suspend fun nullifyToken(pk: Int) {
        return authTokenDao.nullifyToken(pk)
    }

    override suspend fun searchTokenByPk(pk: Int): AuthTokenEntity? {
        return authTokenDao.searchTokenByPk(pk)
    }
}