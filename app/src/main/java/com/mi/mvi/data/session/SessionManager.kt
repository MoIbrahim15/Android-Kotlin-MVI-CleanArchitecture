package com.mi.mvi.data.session

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mi.mvi.data.database.AuthTokenDao
import com.mi.mvi.data.models.AuthToken
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
        Log.e("MVI>", "LOGOUT")

        GlobalScope.launch(IO) {
            var errorMessage: String? = null

            try {
                _cachedToken.value?.account_pk?.let { pk ->
                    authTokenDao.nullifyToken(pk)
                }
            } catch (e: CancellationException) {
                Log.e("MVI", "LOGOUT CancellationException")
                errorMessage = e.message
            } catch (e: Exception) {
                Log.e("MVI", "LOGOUT Exception")
                errorMessage = errorMessage + "\n" + e.message
            } finally {
                errorMessage?.let { Log.e("MVI", errorMessage) }
            }
            Log.e("MVI", "LOGOUT finally")
            setValue(null)
        }
    }

    private fun setValue(newValue: AuthToken?) {
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
                        Log.i("update_status", "Network is available : true")
                        return it.isConnected
                    } ?: false
                } catch (e: java.lang.Exception) {
                    Log.i("update_status", "" + e.message)
                }
            }
        } catch (e: Exception) {
            Log.e("MVI", "No Connection")
        }
        return false
    }
}