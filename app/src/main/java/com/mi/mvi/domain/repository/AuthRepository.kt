package com.mi.mvi.domain.repository

import com.mi.mvi.presentation.auth.state.AuthViewState
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.coroutines.flow.Flow

interface AuthRepository : BaseRepository {

    fun login(
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>>

    fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>>


    fun checkPreviousAuthUser()
            : Flow<DataState<AuthViewState>>


    fun saveAuthUserToPrefs(email: String)
}