package com.mi.mvi.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RemoteUser(
    @SerializedName("pk") @Expose var pk: Int,
    @SerializedName("email") @Expose var email: String? = null,
    @SerializedName("username") @Expose var username: String? = null,
    @SerializedName("token") @Expose var token: String? = null
) : BaseRemote()