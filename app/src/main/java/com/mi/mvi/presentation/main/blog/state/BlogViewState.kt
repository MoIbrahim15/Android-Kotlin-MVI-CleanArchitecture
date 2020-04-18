package com.mi.mvi.presentation.main.blog.state

import android.net.Uri
import android.os.Parcelable
import com.mi.mvi.cache.entity.BlogPostEntity
import kotlinx.android.parcel.Parcelize

const val BLOG_VIEW_STATE_BUNDLE_KEY = "BLOG_VIEW_STATE_BUNDLE_KEY"

@Parcelize
data class BlogViewState(
    //BlogFragment vars
    var blogFields: BlogFields = BlogFields(),

    //ViewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields(),

    //UpdateBlogFragment vars
    var updatedBlogFields: UpdatedBlogFields = UpdatedBlogFields()
) : Parcelable


@Parcelize
data class BlogFields(
    var blogListEntity: MutableList<BlogPostEntity>? = null,
    var searchQuery: String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var filter: String? = null,
    var order: String? = null,
    var layoutManagerState: Parcelable? = null
) : Parcelable


@Parcelize
data class ViewBlogFields(
    var blogPostEntity: BlogPostEntity? = null,
    var isAuthor: Boolean? = null
) : Parcelable

@Parcelize
data class UpdatedBlogFields(
    var updatedBlogTitle: String? = null,
    var updatedBlogBody: String? = null,
    var updatedImageUri: Uri? = null
) : Parcelable