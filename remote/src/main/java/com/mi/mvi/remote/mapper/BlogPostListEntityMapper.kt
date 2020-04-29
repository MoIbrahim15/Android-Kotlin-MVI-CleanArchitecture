package com.mi.mvi.remote.mapper

import com.mi.mvi.data.entity.BlogPostEntity
import com.mi.mvi.data.entity.BlogPostListEntity
import com.mi.mvi.remote.model.RemoteBlogPostList

/**
 * Map a [RemoteBlogPostList] to and from a [BlogPostListEntity] instance when data is moving between
 * this later and the Data layer
 */
open class BlogPostListEntityMapper : EntityMapper<RemoteBlogPostList, BlogPostListEntity> {

    /**
     * Map an instance of a [RemoteBlogPostList] to a [BlogPostListEntity] model
     */
    override fun mapFromRemote(type: RemoteBlogPostList): BlogPostListEntity {
        val itemsEntity = type.results.map { itemRemote ->
            BlogPostEntity(
                itemRemote.pk,
                itemRemote.title,
                itemRemote.slug,
                itemRemote.body,
                itemRemote.image,
                itemRemote.date_updated,
                itemRemote.username
            )
        }.toMutableList()

        return BlogPostListEntity(itemsEntity, type.detail)
    }
}
