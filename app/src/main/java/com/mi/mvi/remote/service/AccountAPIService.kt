package com.mi.mvi.remote.service

import com.mi.mvi.cache.entity.UserEntity
import com.mi.mvi.remote.entity.BaseResponse
import retrofit2.http.*

interface AccountAPIService {
    @GET("account/properties")
    suspend fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): UserEntity


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