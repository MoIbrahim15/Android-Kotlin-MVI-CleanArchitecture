package com.mi.mvi.data.datasource.cache

import com.mi.mvi.data.entity.TokenEntity

interface TokenCacheDataSource {

    suspend fun insert(tokenEntity: TokenEntity): Long

    suspend fun nullifyToken(pk: Int)

    suspend fun searchTokenByPk(pk: Int): TokenEntity?
}
