package com.mi.mvi.domain.model

data class BlogPostList(
    var results: MutableList<BlogPost>?,
    var detail: String?
)
