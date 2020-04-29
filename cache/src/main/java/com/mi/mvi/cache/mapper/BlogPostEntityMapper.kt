package com.mi.mvi.cache.mapper

import com.mi.mvi.cache.model.CachedBlogPost
import com.mi.mvi.data.entity.BlogPostEntity

/**
 * Map a [CachedBlogPost] instance to and from a [BlogPostEntity] instance when data is moving between
 * this later and the Data layer
 */
class BlogPostEntityMapper : EntityMapper<CachedBlogPost, BlogPostEntity> {

    /**
     * Map a [CachedBlogPost] instance to a [BlogPostEntity] instance
     */
    override fun mapFromCached(type: CachedBlogPost): BlogPostEntity {
        return BlogPostEntity(
            type.pk,
            type.title,
            type.slug,
            type.body,
            type.image,
            type.getDateAsString(),
            type.username
        )
    }

    /**
     * Map a [BlogPostEntity] instance to a [CachedBlogPost] instance
     */
    override fun mapToCached(type: BlogPostEntity): CachedBlogPost {
        return CachedBlogPost(
            type.pk,
            type.title,
            type.slug,
            type.body,
            type.image,
            type.getDateAsLong(),
            type.username
        )
    }
}
