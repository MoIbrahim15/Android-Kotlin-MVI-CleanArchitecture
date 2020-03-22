package com.mi.mvi.ui.main.create_blog

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mi.mvi.R
import com.mi.mvi.ui.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CreateBlogFragment : BaseFragment(R.layout.fragment_create_blog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.createBlogFragment, activity as AppCompatActivity)
    }
}