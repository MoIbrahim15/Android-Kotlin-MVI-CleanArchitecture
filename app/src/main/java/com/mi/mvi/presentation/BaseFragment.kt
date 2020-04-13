package com.mi.mvi.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.mi.mvi.presentation.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class BaseFragment(val contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected var dataStateChangeListener: DataStateChangeListener? = null
    protected var uiCommunicationListener: UICommunicationListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(activity = activity as AppCompatActivity)
    }

    private fun setupActionBarWithNavController(activity: AppCompatActivity) {
        if (activity is MainActivity) {
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
}