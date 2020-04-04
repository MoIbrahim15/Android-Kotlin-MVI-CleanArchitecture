package com.mi.mvi.ui.main.blog.state

import com.mi.mvi.data.models.BlogPost

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
    var page : Int = 1,
    var isQueryExhausted : Boolean = false
)


data class ViewBlogFields(
    var blogPost: BlogPost? = null,
    var isAuthor: Boolean = false
)