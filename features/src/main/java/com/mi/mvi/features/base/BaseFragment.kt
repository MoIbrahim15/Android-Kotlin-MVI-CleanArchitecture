package com.mi.mvi.features.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.datastate.MessageType
import com.mi.mvi.domain.datastate.StateMessage
import com.mi.mvi.domain.datastate.UIComponentType
import com.mi.mvi.domain.viewstate.CreateBlogViewState
import com.mi.mvi.features.common.DataStateChangeListener
import com.mi.mvi.features.common.UICommunicationListener
import com.mi.mvi.features.main.MainActivity
import com.mi.mvi.features.main.blog.BlogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseFragment(private val contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected var dataStateChangeListener: DataStateChangeListener? = null
    protected var uiCommunicationListener: UICommunicationListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(activity = activity as AppCompatActivity)
    }

    private fun setupActionBarWithNavController(activity: AppCompatActivity) {
        if (activity is MainActivity && this !is BlogFragment) {
            val appBarConfiguration = AppBarConfiguration(setOf(contentLayoutId))
            NavigationUI.setupActionBarWithNavController(
                activity as AppCompatActivity,
                findNavController(),
                appBarConfiguration
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateChangeListener = context as DataStateChangeListener
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
        }
    }

    fun showErrorDialog(errorMessage: String) {
        dataStateChangeListener?.onDataStateChangeListener(
             DataState.ERROR<CreateBlogViewState>(
                 StateMessage(
                    errorMessage,
                     UIComponentType.DIALOG,
                     MessageType.ERROR
                )
            )
        )
    }
}
