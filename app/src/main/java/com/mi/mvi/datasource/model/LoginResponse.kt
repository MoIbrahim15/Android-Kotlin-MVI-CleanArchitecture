package com.mi.mvi.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    @Expose
    var token: String,

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("email")
    @Expose
    var email: String
) : BaseResponse()