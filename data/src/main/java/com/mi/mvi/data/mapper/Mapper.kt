package com.mi.mvi.data.mapper

/**
 * Interface for model mappers. It provides helper methods that facilitate
 * retrieving of models from outer data source layers
 *
 * @param <Entity> the remote model input type
 * @param <Model> the model return type
 */
interface Mapper<Entity, Model> {

    fun mapFromEntity(type: Entity): Model

    fun mapToEntity(type: Model): Entity

}