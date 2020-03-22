package com.mi.mvi.ui

import android.content.Context
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.mi.mvi.data.session.SessionManager
import org.koin.android.ext.android.inject

abstract class BaseFragment(@LayoutRes  contentLayoutId : Int) : Fragment(contentLayoutId) {

    private var TAG: String = "BaseFragment";
    protected var dataStateChanged: DataStateChangeListener? = null
    val sessionManager: SessionManager by inject()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateChanged = context as DataStateChangeListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }
    }
}