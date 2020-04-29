package com.mi.mvi.domain.repository

import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.viewstate.AuthViewState
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

    fun checkPreviousAuthUser(): Flow<DataState<AuthViewState>>

    suspend fun nullifyToken(pk: Int)
}
