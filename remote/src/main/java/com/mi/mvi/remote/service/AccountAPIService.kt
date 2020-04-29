package com.mi.mvi.remote.service

import com.mi.mvi.remote.model.BaseRemote
import com.mi.mvi.remote.model.RemoteUser
import retrofit2.http.*

interface AccountAPIService {
    @GET("account/properties")
    suspend fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): RemoteUser

    @PUT("account/properties/update")
    @FormUrlEncoded
    suspend fun updateAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String?,
        @Field("username") username: String?
    ): BaseRemote

    @PUT("account/change_password/")
    @FormUrlEncoded
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): BaseRemote
}
