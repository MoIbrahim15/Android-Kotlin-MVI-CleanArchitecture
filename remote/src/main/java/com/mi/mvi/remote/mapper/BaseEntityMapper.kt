package com.mi.mvi.remote.mapper

import com.mi.mvi.data.entity.BaseEntity
import com.mi.mvi.remote.model.BaseRemote


/**
 * Map a [BaseRemote] to and from a [BaseEntity] instance when data is moving between
 * this later and the Data layer
 */
open class BaseEntityMapper : EntityMapper<BaseRemote, BaseEntity> {

    /**
     * Map an instance of a [BaseRemote] to a [BaseEntity] model
     */
    override fun mapFromRemote(type: BaseRemote): BaseEntity {
        val baseEntity = BaseEntity()
        baseEntity.response = type.response
        baseEntity.errorMessage = type.errorMessage
        return baseEntity
    }

}