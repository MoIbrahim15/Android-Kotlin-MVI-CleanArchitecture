package com.mi.mvi.eventstate

import okhttp3.MultipartBody

sealed class CreateBlogEventState {

    data class CreateNewBlogEvent(
        val title: String,
        val body: String,
        val image: MultipartBody.Part
    ) : CreateBlogEventState()

    object None : CreateBlogEventState()
}
