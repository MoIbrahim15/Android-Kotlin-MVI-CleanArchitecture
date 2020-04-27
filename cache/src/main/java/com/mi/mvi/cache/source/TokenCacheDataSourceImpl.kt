package com.mi.mvi.cache.source

import com.mi.mvi.cache.db.AuthTokenDao
import com.mi.mvi.cache.mapper.TokenEntityMapper
import com.mi.mvi.data.datasource.cache.TokenCacheDataSource
import com.mi.mvi.data.entity.TokenEntity

class TokenCacheDataSourceImpl(
    private val authTokenDao: AuthTokenDao,
    private val tokenEntityMapper: TokenEntityMapper
) : TokenCacheDataSource {

    override suspend fun insert(tokenEntity: TokenEntity): Long {
        return authTokenDao.insert(tokenEntityMapper.mapToCached(tokenEntity))
    }

    override suspend fun nullifyToken(pk: Int) {
        return authTokenDao.nullifyToken(pk)
    }

    override suspend fun searchTokenByPk(pk: Int): TokenEntity? {
        authTokenDao.searchTokenByPk(pk)?.let { cachedToken ->
            return tokenEntityMapper.mapFromCached(cachedToken)
        } ?: return null
    }
}
