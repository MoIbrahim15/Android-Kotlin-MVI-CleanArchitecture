package com.mi.mvi.presentation.main.blog.viewmodel

import android.net.Uri
import com.mi.mvi.datasource.model.BlogPost
import com.mi.mvi.utils.BlogQueryUtils.Companion.BLOG_FILTER_DATE_UPDATED
import com.mi.mvi.utils.BlogQueryUtils.Companion.BLOG_ORDER_DESC
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun BlogViewModel.getIsQueryExhausted(): Boolean {
    return getCurrentViewStateOrNew().blogFields?.isQueryExhausted
        ?: false
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getFilter(): String {
    return getCurrentViewStateOrNew().blogFields?.filter
        ?: BLOG_FILTER_DATE_UPDATED
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getOrder(): String {
    return getCurrentViewStateOrNew().blogFields?.order
        ?: BLOG_ORDER_DESC
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().blogFields?.searchQuery
        ?: return ""
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getPage(): Int {
    return getCurrentViewStateOrNew().blogFields?.page
        ?: return 1
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getSlug(): String {
    getCurrentViewStateOrNew().let {
        it.viewBlogFields?.blogPost?.let {
            return it.slug
        }
    }
    return ""
}

@ExperimentalCoroutinesApi
fun BlogViewModel.isAuthorOfBlogPost(): Boolean {
    return getCurrentViewStateOrNew().viewBlogFields?.isAuthor
        ?: false
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getBlogPost(): BlogPost {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields?.blogPost?.let {
            return it
        } ?: getDummyBlogPost()
    }
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getDummyBlogPost(): BlogPost {
    return BlogPost(-1, "", "", "", "", 1, "")
}

@ExperimentalCoroutinesApi
fun BlogViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updatedBlogFields?.updatedImageUri?.let {
            return it
        }
    }
    return null
}