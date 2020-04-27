package com.mi.mvi.domain.viewstate


import com.mi.mvi.domain.model.BlogPost

const val BLOG_VIEW_STATE_BUNDLE_KEY = "BLOG_VIEW_STATE_BUNDLE_KEY"

data class BlogViewState(
    // BlogFragment vars
    var blogFields: BlogFields = BlogFields(),

    // ViewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields(),

    // UpdateBlogFragment vars
    var updatedBlogFields: UpdatedBlogFields = UpdatedBlogFields()
)

data class BlogFields(
    var blogList: MutableList<BlogPost>? = null,
    var searchQuery: String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var filter: String? = null,
    var order: String? = null
)

data class ViewBlogFields(
    var blogPostEntity: BlogPost? = null,
    var isAuthor: Boolean? = null
)

data class UpdatedBlogFields(
    var updatedBlogTitle: String? = null,
    var updatedBlogBody: String? = null,
    var updatedImageUri: String? = null
)