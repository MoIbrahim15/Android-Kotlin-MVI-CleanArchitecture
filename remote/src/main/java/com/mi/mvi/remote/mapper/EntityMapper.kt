package com.mi.mvi.remote.mapper

/**
 * Interface for model mappers. It provides helper methods that facilitate
 * retrieving of models from outer data source layers
 *
 * @param <REMOTE> the remote model input type
 * @param <ENTITY> the entity model output type
 */
interface EntityMapper<in REMOTE, out ENTITY> {

    fun mapFromRemote(type: REMOTE): ENTITY
}