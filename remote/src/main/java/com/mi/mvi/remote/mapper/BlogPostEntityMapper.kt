package com.mi.mvi.remote.mapper

import com.mi.mvi.data.entity.BlogPostEntity
import com.mi.mvi.remote.model.RemoteBlogPost


/**
 * Map a [RemoteBlogPost] to and from a [BlogPostEntity] instance when data is moving between
 * this later and the Data layer
 */
open class BlogPostEntityMapper : EntityMapper<RemoteBlogPost, BlogPostEntity> {

    /**
     * Map an instance of a [RemoteBlogPost] to a [BlogPostEntity] model
     */
    override fun mapFromRemote(type: RemoteBlogPost): BlogPostEntity {
        val blogPostEntity = BlogPostEntity(
            type.pk,
            type.title,
            type.slug,
            type.body,
            type.image,
            type.date_updated,
            type.username
        )
        blogPostEntity.response = type.response
        return blogPostEntity
    }

}