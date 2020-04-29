package com.mi.mvi.mapper

import com.mi.mvi.domain.model.User
import com.mi.mvi.model.TokenView
import com.mi.mvi.model.UserView

/**
 * Map a [TokenView] to and from a [TokenEntity] instance when data is moving between
 * this later and the Data layer
 */
open class UserMapper : Mapper<UserView, User> {
    override fun mapToView(type: User): UserView {
        return UserView(
            type.pk,
            type.email,
            type.username,
            type.token
        )
    }

    override fun mapFromView(type: UserView): User {
        return User(
            type.pk,
            type.email,
            type.username,
            type.token
        )
    }
}
