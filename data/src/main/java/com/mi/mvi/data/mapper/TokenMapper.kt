package com.mi.mvi.data.mapper

import com.mi.mvi.data.entity.TokenEntity
import com.mi.mvi.data.entity.UserEntity
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.model.User

/**
 * Map a [User] to and from a [UserEntity] instance when data is moving between
 * this later and the Data layer
 */
open class TokenMapper : Mapper<TokenEntity, Token> {

    override fun mapFromEntity(type: TokenEntity): Token {
        return Token(type.account_pk, type.token)
    }

    override fun mapToEntity(type: Token): TokenEntity {
        return TokenEntity(type.account_pk, type.token)
    }
}
