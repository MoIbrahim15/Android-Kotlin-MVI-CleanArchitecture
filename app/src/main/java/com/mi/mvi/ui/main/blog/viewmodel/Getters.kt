package com.mi.mvi.ui.main.blog.viewmodel

import com.mi.mvi.data.models.BlogPost
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun BlogViewModel.getFilter() :  String{
    getCurrentViewStateOrNew()?.let {
        return it.blogsFields.filter
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getOrder() :  String{
    getCurrentViewStateOrNew()?.let {
        return it.blogsFields.order
    }
}


@ExperimentalCoroutinesApi
fun BlogViewModel.getSearchQuery() :  String{
    getCurrentViewStateOrNew()?.let {
        return it.blogsFields.searchQuery
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getPage() :  Int{
    getCurrentViewStateOrNew()?.let {
        return it.blogsFields.page
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getIsQueryExhausted() : Boolean{
    getCurrentViewStateOrNew()?.let {
        return it.blogsFields.isQueryExhausted
    }
}


@ExperimentalCoroutinesApi
fun BlogViewModel.getSlug(): String{
    getCurrentViewStateOrNew().let {
        it.viewBlogFields.blogPost?.let {
            return it.slug
        }
    }
    return ""
}

@ExperimentalCoroutinesApi
fun BlogViewModel.isAuthorOfBlogPost(): Boolean{
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.isAuthor
    }
}


@ExperimentalCoroutinesApi
fun BlogViewModel.getBlogPost(): BlogPost {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPost?.let {
            return it
        }?: getDummyBlogPost()
    }
}

fun BlogViewModel.getDummyBlogPost(): BlogPost{
    return BlogPost(-1, "" , "", "", "", 1, "")
}