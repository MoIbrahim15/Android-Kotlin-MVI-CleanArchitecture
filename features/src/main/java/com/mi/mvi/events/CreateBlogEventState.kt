package com.mi.mvi.events

import okhttp3.MultipartBody

sealed class CreateBlogEventState {

    data class CreateNewBlogEvent(
        val title: String,
        val body: String,
        val image: MultipartBody.Part
    ) : CreateBlogEventState()

    object None : CreateBlogEventState()
}
