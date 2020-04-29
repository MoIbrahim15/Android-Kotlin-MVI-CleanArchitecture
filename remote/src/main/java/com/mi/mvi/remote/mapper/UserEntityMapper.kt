package com.mi.mvi.remote.mapper

import com.mi.mvi.data.entity.BaseEntity
import com.mi.mvi.data.entity.UserEntity
import com.mi.mvi.remote.model.BaseRemote
import com.mi.mvi.remote.model.RemoteUser

/**
 * Map a [BaseRemote] to and from a [BaseEntity] instance when data is moving between
 * this later and the Data layer
 */
open class UserEntityMapper : EntityMapper<RemoteUser, UserEntity> {

    /**
     * Map an instance of a [RemoteUser] to a [BaseEntityMapper] model
     */
    override fun mapFromRemote(type: RemoteUser): UserEntity {
        val userEntity = UserEntity(
            type.pk,
            type.email,
            type.username,
            type.token
        )
        userEntity.response = type.response
        userEntity.errorMessage = type.errorMessage
        return userEntity
    }
}
