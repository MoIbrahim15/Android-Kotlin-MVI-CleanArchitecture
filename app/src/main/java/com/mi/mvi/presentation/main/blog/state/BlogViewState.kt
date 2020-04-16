package com.mi.mvi.presentation.main.blog.state

import android.net.Uri
import android.os.Parcelable
import com.mi.mvi.datasource.model.BlogPost
import com.mi.mvi.utils.BlogQueryUtils.Companion.BLOG_ORDER_ASC
import com.mi.mvi.utils.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED
import kotlinx.android.parcel.Parcelize

const val BLOG_VIEW_STATE_BUNDLE_KEY = "BLOG_VIEW_STATE_BUNDLE_KEY"

@Parcelize
data class BlogViewState(
    //BlogFragment vars
    var blogsFields: BlogFields = BlogFields(),

    //ViewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields(),

    //UpdateBlogFragment vars
    var updatedBlogFields: UpdatedBlogFields = UpdatedBlogFields()
) : Parcelable


@Parcelize
data class BlogFields(
    var blogList: MutableList<BlogPost> = mutableListOf(),
    var searchQuery: String = "",
    var page: Int = 1,
    var isQueryExhausted: Boolean = false,
    var filter: String = ORDER_BY_ASC_DATE_UPDATED,
    var order: String = BLOG_ORDER_ASC,
    var layoutManagerState: Parcelable? = null
) : Parcelable


@Parcelize
data class ViewBlogFields(
    var blogPost: BlogPost? = null,
    var isAuthor: Boolean = false
) : Parcelable

@Parcelize
data class UpdatedBlogFields(
    var updatedBlogTitle: String? = null,
    var updatedBlogBody: String? = null,
    var updatedImageUri: Uri? = null
) : Parcelable