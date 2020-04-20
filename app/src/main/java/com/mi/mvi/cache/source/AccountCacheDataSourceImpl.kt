package com.mi.mvi.cache.source

import com.mi.mvi.cache.db.AccountDao
import com.mi.mvi.cache.entity.UserEntity
import com.mi.mvi.data.datasource.cache.AccountCacheDataSource

class AccountCacheDataSourceImpl(
    private val accountDao: AccountDao
) : AccountCacheDataSource {

    override suspend fun insertOrIgnore(userEntity: UserEntity): Long {
        return accountDao.insertOrIgnore(userEntity)
    }

    override suspend fun searchByPk(pk: Int): UserEntity? {
        return accountDao.searchByPk(pk)
    }

    override suspend fun searchByEmail(email: String): UserEntity? {
        return accountDao.searchByEmail(email)
    }

    override suspend fun updateAccountProperties(pk: Int, email: String, username: String) {
        return accountDao.updateAccountProperties(pk, email, username)
    }
}
