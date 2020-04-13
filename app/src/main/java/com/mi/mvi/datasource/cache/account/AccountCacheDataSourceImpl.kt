package com.mi.mvi.datasource.cache.account

import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.datasource.model.AccountProperties

class AccountCacheDataSourceImpl(
    private val accountDao: AccountDao
) : AccountCacheDataSource {

    override suspend fun insertOrIgnore(accountProperties: AccountProperties): Long {
        return accountDao.insertOrIgnore(accountProperties)
    }

    override suspend fun searchByPk(pk: Int): AccountProperties? {
        return accountDao.searchByPk(pk)
    }

    override suspend fun searchByEmail(email: String): AccountProperties? {
        return accountDao.searchByEmail(email)
    }

    override suspend fun updateAccountProperties(pk: Int, email: String, username: String) {
        return accountDao.updateAccountProperties(pk, email, username)
    }

}