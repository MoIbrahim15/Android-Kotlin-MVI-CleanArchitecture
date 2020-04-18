package com.mi.mvi.presentation.main.blog.viewmodel

import android.net.Uri
import android.os.Parcelable
import com.mi.mvi.cache.entity.BlogPostEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun BlogViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.blogFields?.searchQuery = query
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogListData(blogListEntity: MutableList<BlogPostEntity>){
    val update = getCurrentViewStateOrNew()
    update.blogFields?.blogListEntity = blogListEntity
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogPost(blogPostEntity: BlogPostEntity){
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields?.blogPostEntity = blogPostEntity
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setIsAuthorOfBlogPost(isAuthorOfBlogPost: Boolean){
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields?.isAuthor = isAuthorOfBlogPost
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields?.isQueryExhausted = isExhausted
    setViewState(update)
}


// Filter can be "date_updated" or "username"
@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogFilter(filter: String?){
    filter?.let{
        val update = getCurrentViewStateOrNew()
        update.blogFields?.filter = filter
        setViewState(update)
    }
}

// Order can be "-" or ""
// Note: "-" = DESC, "" = ASC
@ExperimentalCoroutinesApi
fun BlogViewModel.setBlogOrder(order: String?){
    val update = getCurrentViewStateOrNew()
    update.blogFields?.order = order
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.blogFields?.layoutManagerState = layoutManagerState
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.blogFields?.layoutManagerState = null
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.removeDeletedBlogPost() {
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields?.blogListEntity?.toMutableList()
    if(list != null){
        for(i in 0..(list.size - 1)){
            if(list[i] == getBlogPost()){
                list.remove(getBlogPost())
                break
            }
        }
        setBlogListData(list)
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.updateListItem(){
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields?.blogListEntity?.toMutableList()
    if(list != null){
        val newBlogPost = getBlogPost()
        for(i in 0..(list.size - 1)){
            if(list[i].pk == newBlogPost.pk){
                list[i] = newBlogPost
                break
            }
        }
        update.blogFields?.blogListEntity = list
        setViewState(update)
    }
}


@ExperimentalCoroutinesApi
fun BlogViewModel.setUpdatedUri(uri: Uri){
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    updatedBlogFields?.updatedImageUri = uri
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}

@ExperimentalCoroutinesApi
fun BlogViewModel.setUpdatedTitle(title: String){
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    updatedBlogFields?.updatedBlogTitle = title
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}


@ExperimentalCoroutinesApi
fun BlogViewModel.setUpdatedBody(body: String){
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    updatedBlogFields?.updatedBlogBody = body
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}