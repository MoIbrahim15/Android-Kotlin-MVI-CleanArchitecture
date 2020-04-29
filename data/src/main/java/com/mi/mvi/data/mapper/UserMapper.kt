package com.mi.mvi.data.mapper

import com.mi.mvi.data.entity.TokenEntity
import com.mi.mvi.data.entity.UserEntity
import com.mi.mvi.domain.model.Token
import com.mi.mvi.domain.model.User

/**
 * Map a [Token] to and from a [TokenEntity] instance when data is moving between
 * this later and the Data layer
 */
open class UserMapper : Mapper<UserEntity, User> {
    override fun mapFromEntity(type: UserEntity): User {
        return User(
            type.pk,
            type.email,
            type.username,
            type.token
        )
    }

    override fun mapToEntity(type: User): UserEntity {
        return UserEntity(
            type.pk,
            type.email,
            type.username,
            type.token
        )
    }
}
