package com.mi.mvi.ui.main.blog.state

import com.mi.mvi.data.models.BlogPost
import com.mi.mvi.utils.BlogQueryUtils.Companion.BLOG_ORDER_ASC
import com.mi.mvi.utils.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED

data class BlogViewState(
    //BlogFragment vars
    var blogsFields: BlogFields = BlogFields(),

    //ViewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields()

    //UpdateBlogFragment vars
)


data class BlogFields(
    var blogList: MutableList<BlogPost> = mutableListOf(),
    var searchQuery: String = "",
    var page: Int = 1,
    var isQueryExhausted: Boolean = false,
    var filter: String = ORDER_BY_ASC_DATE_UPDATED,
    var order: String = BLOG_ORDER_ASC)


data class ViewBlogFields(
    var blogPost: BlogPost? = null,
    var isAuthor: Boolean = false
)