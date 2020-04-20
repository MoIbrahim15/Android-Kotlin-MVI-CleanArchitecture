package com.mi.mvi.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mi.mvi.cache.db.AuthTokenDao
import com.mi.mvi.cache.entity.AuthTokenEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SessionManager(
    val authTokenDao: AuthTokenDao,
    val context: Context
) {
    private val _cachedToken = MutableLiveData<AuthTokenEntity>()

    val cachedTokenEntity: LiveData<AuthTokenEntity>
        get() = _cachedToken

    fun login(newValue: AuthTokenEntity) {
        setValue(newValue)
    }

    fun logout() {
        GlobalScope.launch(IO) {
            var errorMessage: String? = null

            try {
                _cachedToken.value?.account_pk?.let { pk ->
                    authTokenDao.nullifyToken(pk)
                }
            } catch (e: CancellationException) {
                errorMessage = e.message
            } catch (e: Exception) {
                errorMessage = errorMessage + "\n" + e.message
            } finally {
            }
            setValue(null)
        }
    }

    fun setValue(newValue: AuthTokenEntity?) {
        GlobalScope.launch(Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue
            }
        }
    }
}
