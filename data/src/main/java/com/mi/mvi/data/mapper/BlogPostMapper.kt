package com.mi.mvi.data.mapper

import com.mi.mvi.data.entity.BlogPostEntity
import com.mi.mvi.domain.model.BlogPost

/**
 * Map a [BlogPost] to and from a [BlogPostEntity] instance when data is moving between
 * this later and the Data layer
 */
open class BlogPostMapper : Mapper<BlogPostEntity, BlogPost> {
    override fun mapFromEntity(type: BlogPostEntity): BlogPost {
        return BlogPost(
            type.pk,
            type.title,
            type.slug,
            type.body,
            type.image,
            type.date_updated,
            type.username
        )
    }

    override fun mapToEntity(type: BlogPost): BlogPostEntity {
        return BlogPostEntity(
            type.pk,
            type.title,
            type.slug,
            type.body,
            type.image,
            type.date_updated,
            type.username
        )
    }

}