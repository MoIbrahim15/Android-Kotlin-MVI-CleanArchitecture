package com.mi.mvi.cache.mapper

import com.mi.mvi.cache.model.CachedToken
import com.mi.mvi.data.entity.TokenEntity


/**
 * Map a [CachedToken] instance to and from a [TokenEntity] instance when data is moving between
 * this later and the Data layer
 */
class TokenEntityMapper : EntityMapper<CachedToken, TokenEntity> {

    /**
     * Map a [CachedToken] instance to a [TokenEntity] instance
     */
    override fun mapFromCached(type: CachedToken): TokenEntity {
        return TokenEntity(type.account_pk, type.token)
    }

    /**
     * Map a [TokenEntity] instance to a [CachedToken] instance
     */
    override fun mapToCached(type: TokenEntity): CachedToken {
        return CachedToken(type.account_pk, type.token)
    }

}