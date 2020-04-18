package com.mi.mvi.presentation.main.create_blog.state

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

const val CREATE_BLOG_VIEW_STATE_BUNDLE_KEY = "CREATE_BLOG_VIEW_STATE_BUNDLE_KEY"

@Parcelize
data class CreateBlogViewState(
    var newBlogField: NewBlogFields? = null
) : Parcelable

@Parcelize
data class NewBlogFields(
    var newBlogTitle: String? = null,
    var newBlogBody: String? = null,
    var newImageUri: Uri? = null
) : Parcelable