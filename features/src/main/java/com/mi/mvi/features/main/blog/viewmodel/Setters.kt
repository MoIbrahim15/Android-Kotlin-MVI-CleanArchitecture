package com.mi.mvi.features.main.blog.viewmodel

import android.net.Uri
import android.os.Parcelable
import com.mi.mvi.domain.model.BlogPostView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setQuery(query: String) {
    val update = getCurrentViewStateOrNew()
    update.blogFields?.searchQuery = query
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogListData(blogList: MutableList<BlogPostView>) {
    val update = getCurrentViewStateOrNew()
    update.blogFields?.blogList = blogList.map { blogPostMapper.mapFromView(it) }.toMutableList()

    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogPost(blogPost: BlogPostView) {
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields?.blogPostEntity = blogPostMapper.mapFromView(blogPost)
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setIsAuthorOfBlogPost(isAuthorOfBlogPost: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields?.isAuthor = isAuthorOfBlogPost
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setQueryExhausted(isExhausted: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.blogFields?.isQueryExhausted = isExhausted
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogFilter(filter: String?) {
    filter?.let {
        val update = getCurrentViewStateOrNew()
        update.blogFields?.filter = filter
        setViewState(update)
    }
}

// Order can be "-" or ""
// Note: "-" = DESC, "" = ASC
@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogOrder(order: String?) {
    val update = getCurrentViewStateOrNew()
    update.blogFields?.order = order
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.removeDeletedBlogPost() {
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields?.blogList?.toMutableList()
    if (list != null) {
        for (i in 0..(list.size - 1)) {
            if (list[i] == getBlogPost()) {
                list.remove(getBlogPost())
                break
            }
        }
        setBlogListData(list.map { blogPostMapper.mapToView(it) }.toMutableList())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.updateListItem() {
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields?.blogList?.toMutableList()
    if (list != null) {
        val newBlogPost = getBlogPost()
        for (i in 0..(list.size - 1)) {
            if (list[i].pk == newBlogPost.pk) {
                list[i] = newBlogPost
                break
            }
        }
        update.blogFields?.blogList = list
        setViewState(update)
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setUpdatedUri(uri: Uri) {
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    updatedBlogFields?.updatedImageUri = uri.toString()
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setUpdatedTitle(title: String) {
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    updatedBlogFields?.updatedBlogTitle = title
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.setUpdatedBody(body: String) {
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    updatedBlogFields?.updatedBlogBody = body
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}
