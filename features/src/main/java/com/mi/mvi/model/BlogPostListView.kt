package com.mi.mvi.model

import com.mi.mvi.domain.model.BlogPostView

data class BlogPostListView(
    var results: MutableList<BlogPostView>?,
    var detail: String?
)
