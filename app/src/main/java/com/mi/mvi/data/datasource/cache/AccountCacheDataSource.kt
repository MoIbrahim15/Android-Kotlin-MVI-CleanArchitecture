package com.mi.mvi.data.datasource.cache

import com.mi.mvi.datasource.model.AccountProperties

interface AccountCacheDataSource {

    suspend fun insertOrIgnore(accountProperties: AccountProperties): Long

    suspend fun searchByPk(pk: Int): AccountProperties?

    suspend fun searchByEmail(email: String): AccountProperties?

    suspend fun updateAccountProperties(pk: Int, email: String, username: String)

}