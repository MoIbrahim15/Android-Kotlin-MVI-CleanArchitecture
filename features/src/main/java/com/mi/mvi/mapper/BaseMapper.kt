package com.mi.mvi.mapper

import com.mi.mvi.domain.model.BaseModel
import com.mi.mvi.model.BaseModelView

/**
 * Map a [BaseEntity] to and from a [BaseModelView] instance when data is moving between
 * this later and the Data layer
 */
open class BaseMapper : Mapper<BaseModelView, BaseModel> {

    override fun mapFromView(type: BaseModelView): BaseModel {
        val baseModel = BaseModel()
        baseModel.response = type.response
        baseModel.errorMessage = type.errorMessage
        return baseModel
    }

    override fun mapToView(type: BaseModel): BaseModelView {
        val baseModelView = BaseModelView()
        baseModelView.response = type.response
        baseModelView.errorMessage = type.errorMessage
        return baseModelView
    }
}
