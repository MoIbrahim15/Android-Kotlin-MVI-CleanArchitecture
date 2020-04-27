package com.mi.mvi.domain.viewstate


data class CreateBlogViewState(
    var newBlogField: NewBlogFields = NewBlogFields()
)

data class NewBlogFields(
    var newBlogTitle: String? = null,
    var newBlogBody: String? = null,
    var newImageUri: String?  = null
)
