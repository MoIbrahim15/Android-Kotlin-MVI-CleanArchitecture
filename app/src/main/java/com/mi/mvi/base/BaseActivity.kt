package com.mi.mvi.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mi.mvi.data.session.SessionManager
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {

    val sessionManager : SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
    }

    abstract fun getLayoutRes(): Int
}