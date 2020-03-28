package com.mi.mvi.data.network.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BlogListSearchResponse(
    @SerializedName("results")
    @Expose
    var results: MutableList<BlogSearchResponse>,

    @SerializedName("detail")
    @Expose
    var detail: String
)