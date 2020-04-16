package com.mi.mvi.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mi.mvi.datasource.cache.auth.AuthTokenDao
import com.mi.mvi.datasource.model.AuthToken
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SessionManager(
    val authTokenDao: AuthTokenDao,
    val context: Context
) {
    private val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken


    fun login(newValue: AuthToken) {
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

    fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue
            }
        }
    }

    fun isConnectedToInternet(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities: NetworkCapabilities? = cm.getNetworkCapabilities(cm.activeNetwork)
                capabilities?.let {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    ) {
                        return true
                    }
                }
            } else {
                try {
                    val activeNetworkInfo: NetworkInfo? = cm.activeNetworkInfo
                    activeNetworkInfo?.let {
                        return it.isConnected
                    } ?: return false
                } catch (e: java.lang.Exception) {

                }
            }
        } catch (e: Exception) {

        }
        return false
    }
}