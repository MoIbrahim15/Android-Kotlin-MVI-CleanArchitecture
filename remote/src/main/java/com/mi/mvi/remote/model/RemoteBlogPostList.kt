package com.mi.mvi.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RemoteBlogPostList(
    @SerializedName("results")
    @Expose
    var results: MutableList<RemoteBlogPost>,
    @SerializedName("detail")
    @Expose var detail: String
)
