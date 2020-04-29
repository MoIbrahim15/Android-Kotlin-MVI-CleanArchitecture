package com.mi.mvi.mapper


import com.mi.mvi.domain.model.Token
import com.mi.mvi.model.TokenView
import com.mi.mvi.model.UserView

/**
 * Map a [UserView] to and from a [UserEntity] instance when data is moving between
 * this later and the Data layer
 */
open class TokenMapper : Mapper<TokenView, Token> {

    override fun mapFromView(type: TokenView): Token {
        return Token(type.account_pk, type.token)
    }

    override fun mapToView(type: Token): TokenView {
        return TokenView(type.account_pk, type.token)
    }

}