package com.mi.mvi.common

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mi.mvi.domain.repository.AuthRepository
import com.mi.mvi.model.TokenView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SessionManager(
    private val authRepository: AuthRepository,
    val context: Context
) {
    private val _cachedToken = MutableLiveData<TokenView>()

    val cachedTokenViewEntity: LiveData<TokenView>
        get() = _cachedToken

    fun login(newValue: TokenView) {
        setValue(newValue)
    }

    fun logout() {
        GlobalScope.launch(IO) {
            var errorMessage: String? = null

            try {
                _cachedToken.value?.account_pk?.let { pk ->
                    authRepository.nullifyToken(pk)
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

    fun setValue(newValue: TokenView?) {
        GlobalScope.launch(Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue
            }
        }
    }
}
