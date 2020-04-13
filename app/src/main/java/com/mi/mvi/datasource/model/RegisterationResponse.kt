package com.mi.mvi.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("token")
    @Expose
    var token: String
) : BaseResponse()

