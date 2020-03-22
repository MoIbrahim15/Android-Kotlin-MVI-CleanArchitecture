package com.mi.mvi.ui.main.blog

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mi.mvi.R
import com.mi.mvi.ui.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UpdateBlogFragment : BaseFragment(R.layout.fragment_update_blog){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.blogFragment, activity as AppCompatActivity)
    }
}