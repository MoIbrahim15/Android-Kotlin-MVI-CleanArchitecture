package com.mi.mvi.mapper

import com.mi.mvi.domain.model.BlogPost
import com.mi.mvi.domain.model.BlogPostView
import com.mi.mvi.mapper.Mapper

/**
 * Map a [BlogPostView] to and from a [BlogPostEntity] instance when data is moving between
 * this later and the Data layer
 */
open class BlogPostMapper : Mapper<BlogPostView, BlogPost> {
    override fun mapFromView(type: BlogPostView): BlogPost {
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

    override fun mapToView(type: BlogPost):  BlogPostView{
        return BlogPostView(
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