package com.mi.mvi.presentation.main.create_blog.state

import android.net.Uri

data class CreateBlogViewState(
    var newBlogField: NewBlogFields = NewBlogFields()
)

data class NewBlogFields(
    var newBlogTitle: String? = null,
    var newBlogBody: String? = null,
    var newImageUri: Uri? = null
)