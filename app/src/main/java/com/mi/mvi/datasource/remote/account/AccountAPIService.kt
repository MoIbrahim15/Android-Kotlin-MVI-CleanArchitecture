package com.mi.mvi.datasource.remote.account

import com.mi.mvi.datasource.model.AccountProperties
import com.mi.mvi.datasource.model.BaseResponse
import retrofit2.http.*

interface AccountAPIService {
    @GET("account/properties")
    suspend fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): AccountProperties


    @PUT("account/properties/update")
    @FormUrlEncoded
    suspend fun updateAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("username") username: String
    ): BaseResponse


    @PUT("account/change_password/")
    @FormUrlEncoded
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): BaseResponse
}