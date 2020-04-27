package com.mi.mvi.cache.source

import android.content.SharedPreferences
import com.mi.mvi.cache.db.AccountDao
import com.mi.mvi.cache.mapper.UserEntityMapper
import com.mi.mvi.data.datasource.cache.AccountCacheDataSource
import com.mi.mvi.data.entity.UserEntity

const val PREVIOUS_AUTH_USER: String = "com.mi.mvi.PREVIOUS_AUTH_USER"

class AccountCacheDataSourceImpl(
    private val accountDao: AccountDao,
    private val userEntityMapper: UserEntityMapper,
    private val sharedPreferences: SharedPreferences,
    private val sharedPrefsEditor: SharedPreferences.Editor
) : AccountCacheDataSource {

    override suspend fun insertOrIgnore(userEntity: UserEntity): Long {
        return accountDao.insertOrIgnore(userEntityMapper.mapToCached(userEntity))
    }

    override suspend fun searchByPk(pk: Int): UserEntity? {
        accountDao.searchByPk(pk)?.let { cachedUser ->
            return userEntityMapper.mapFromCached(cachedUser)
        } ?: return null
    }

    override suspend fun searchByEmail(email: String): UserEntity? {
        accountDao.searchByEmail(email)?.let { cachedUser ->
            return userEntityMapper.mapFromCached(cachedUser)
        } ?: return null
    }

    override suspend fun updateAccountProperties(pk: Int, email: String?, username: String?) {
        return accountDao.updateAccountProperties(pk, email, username)
    }

    override fun getLoggedInEmail(): String? {
        return sharedPreferences.getString(PREVIOUS_AUTH_USER, null)
    }

    override fun saveLoggedInEmail(email: String?) {
        sharedPrefsEditor.putString(PREVIOUS_AUTH_USER, email)
        sharedPrefsEditor.apply()
    }
}
