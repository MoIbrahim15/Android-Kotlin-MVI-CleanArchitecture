package com.mi.mvi.ui.main.blog.viewmodel

import com.mi.mvi.data.models.BlogPost
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun BlogViewModel.setQuery(query: String) {
    val update = getCurrentViewStateOrNew()
    update.blogsFields.searchQuery = query
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogList(blogList: MutableList<BlogPost>) {
    val update = getCurrentViewStateOrNew()
    update.blogsFields.blogList = blogList
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogPost(blogPost: BlogPost) {
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.blogPost = blogPost
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setIsAuthorOfBlogPost(isAuthor: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.isAuthor = isAuthor
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setQueryExhausted(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.blogsFields.isQueryExhausted = isExhausted
    setViewState(update)
}
