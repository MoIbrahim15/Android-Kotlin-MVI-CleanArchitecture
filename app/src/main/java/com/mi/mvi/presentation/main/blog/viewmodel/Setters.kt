package com.mi.mvi.presentation.main.blog.viewmodel

import android.net.Uri
import com.mi.mvi.datasource.model.BlogPost
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

@ExperimentalCoroutinesApi
fun BlogViewModel.setFilter(filter: String?) {
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.blogsFields.filter = filter
        setViewState(update)
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setOrder(order: String?) {
    order?.let {
        val update = getCurrentViewStateOrNew()
        update.blogsFields.order = order
        setViewState(update)
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.removeDeleteBlogPost() {
    val update = getCurrentViewStateOrNew()
    val list = update.blogsFields.blogList.toMutableList()
    for (i in 0 until list.size - 1) {
        if (list[i] == getBlogPost()) {
            list.remove(getBlogPost())
            break
        }
    }
    setBlogList(list)
}


@ExperimentalCoroutinesApi
fun BlogViewModel.setUpdatedBlogFields(title: String?, body: String?, uri: Uri?) {
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    title?.let { updatedBlogFields.updatedBlogTitle = it }
    body?.let { updatedBlogFields.updatedBlogBody = it }
    uri?.let { updatedBlogFields.updatedImageUri = it }
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}


@ExperimentalCoroutinesApi
fun BlogViewModel.updateListItem(newBlogPost: BlogPost) {
    val update = getCurrentViewStateOrNew()
    val list = update.blogsFields.blogList.toMutableList()
    for (i in 0 until list.size - 1) {
        if (list[i].pk == newBlogPost.pk) {
            list[i] == newBlogPost
            break
        }
    }
    update.blogsFields.blogList = list
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.onBlogPostUpdatedSuccess(blogPost: BlogPost) {
    setUpdatedBlogFields(
        uri = null,
        title = blogPost.title,
        body = blogPost.body
        )
    setBlogPost(blogPost)
    updateListItem(blogPost)
}