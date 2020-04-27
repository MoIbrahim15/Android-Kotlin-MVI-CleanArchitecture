package com.mi.mvi.cache.mapper

import com.mi.mvi.cache.model.CachedUser
import com.mi.mvi.data.entity.UserEntity


/**
 * Map a [CachedUser] instance to and from a [UserEntity] instance when data is moving between
 * this later and the Data layer
 */
class UserEntityMapper : EntityMapper<CachedUser, UserEntity> {

    /**
     * Map a [CachedUser] instance to a [UserEntity] instance
     */
    override fun mapFromCached(type: CachedUser): UserEntity {
        return UserEntity(type.pk, type.email, type.username)
    }

    /**
     * Map a [UserEntity] instance to a [CachedUser] instance
     */
    override fun mapToCached(type: UserEntity): CachedUser {
        return CachedUser(type.pk, type.email, type.username)
    }

}