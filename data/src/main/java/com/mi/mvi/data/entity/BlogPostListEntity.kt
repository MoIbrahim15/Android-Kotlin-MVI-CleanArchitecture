package com.mi.mvi.data.entity

data class BlogPostListEntity(
    var results: MutableList<BlogPostEntity>,
    var detail: String?
)
