package com.mi.mvi.data.session

import android.content.Context
import com.mi.mvi.data.database.AuthTokenDao

class SessionManager(
    val authTokenDao: AuthTokenDao,
    val application: Context
) {

}