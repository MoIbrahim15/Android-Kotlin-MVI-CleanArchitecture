package com.mi.mvi.features.main.blog.viewmodel

import android.net.Uri
import androidx.core.net.toUri

import com.mi.mvi.domain.model.BlogPost
import com.mi.mvi.features.main.blog.BLOG_FILTER_DATE_UPDATED
import com.mi.mvi.features.main.blog.BLOG_ORDER_DESC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.getIsQueryExhausted(): Boolean {
    return getCurrentViewStateOrNew().blogFields.isQueryExhausted
        ?: false
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.getFilter(): String {
    return getCurrentViewStateOrNew().blogFields.filter
        ?: BLOG_FILTER_DATE_UPDATED
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.getOrder(): String {
    return getCurrentViewStateOrNew().blogFields.order
        ?: BLOG_ORDER_DESC
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().blogFields.searchQuery
        ?: return ""
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.getPage(): Int {
    return getCurrentViewStateOrNew().blogFields.page
        ?: return 1
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.getSlug(): String {
    getCurrentViewStateOrNew().let {
        it.viewBlogFields.blogPostEntity?.let {
            return it.slug!!
        }
    }
    return ""
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.isAuthorOfBlogPost(): Boolean {
    return getCurrentViewStateOrNew().viewBlogFields.isAuthor
        ?: false
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.getBlogPost(): BlogPost {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPostEntity?.let {
            return it
        } ?: getDummyBlogPost()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun getDummyBlogPost(): BlogPost {
    return BlogPost(-1, "", "", "", "", "", "")
}

@FlowPreview
@ExperimentalCoroutinesApi
fun BlogViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updatedBlogFields.updatedImageUri?.let { string ->
            return string.toUri()
        }
    }
    return null
}
