package com.mi.mvi.remote.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BlogListResponse(
    @SerializedName("results")
    @Expose
    var results: MutableList<BlogPostResponse>,

    @SerializedName("detail")
    @Expose
    var detail: String
)