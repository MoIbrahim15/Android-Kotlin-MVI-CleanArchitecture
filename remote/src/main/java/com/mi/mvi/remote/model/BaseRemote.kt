package com.mi.mvi.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseRemote {
    @SerializedName("response") @Expose var response: String? = null
    @SerializedName("error_message") @Expose var errorMessage: String? = null
}
