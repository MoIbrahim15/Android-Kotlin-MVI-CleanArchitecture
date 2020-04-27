package com.mi.mvi.data.mapper

import com.mi.mvi.data.entity.BaseEntity
import com.mi.mvi.domain.model.BaseModel


/**
 * Map a [BaseEntity] to and from a [BaseModel] instance when data is moving between
 * this later and the Data layer
 */
open class BaseMapper : Mapper<BaseEntity, BaseModel> {

    override fun mapFromEntity(type: BaseEntity): BaseModel {
        val baseModel = BaseModel()
        baseModel.response = type.response
        baseModel.errorMessage = type.errorMessage
        return baseModel
    }

    override fun mapToEntity(type: BaseModel): BaseEntity {
        val baseEntity = BaseEntity()
        baseEntity.response = type.response
        baseEntity.errorMessage = type.errorMessage
        return baseEntity
    }
}