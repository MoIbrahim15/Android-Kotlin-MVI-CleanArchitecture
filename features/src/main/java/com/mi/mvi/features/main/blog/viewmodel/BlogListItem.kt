package com.mi.mvi.features.main.blog.viewmodel

import com.mi.mvi.domain.model.BlogPostView

sealed class BlogListItem {
    object NoMoreResult : BlogListItem()
    data class Item(val blogPostView: BlogPostView) : BlogListItem()
}