package com.mi.mvi.data.network.main

import com.mi.mvi.data.models.AccountProperties
import retrofit2.http.GET
import retrofit2.http.Header

interface MainApiService {

    @GET("account/properties")
    fun getAccountProperties(
        @Header("Authorization") authorization : String
    ) : AccountProperties

}