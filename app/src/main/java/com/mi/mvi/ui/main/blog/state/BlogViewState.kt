package com.mi.mvi.ui.main.blog.state

import com.mi.mvi.data.models.BlogPost

data class BlogViewState(
    //BlogFragment vars
    var blogsFields: BlogFields = BlogFields()

    //ViewBlogFragment vars


    //UpdateBlogFragment vars
)


data class BlogFields(
    var blogs: MutableList<BlogPost> = mutableListOf(),
    var searchQuery : String = ""
)