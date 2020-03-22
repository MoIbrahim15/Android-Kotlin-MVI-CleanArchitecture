package com.mi.mvi.ui.main.blog

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_blog.*

class BlogFragment : BaseFragment(R.layout.fragment_blog) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goViewBlogFragment.setOnClickListener {
            findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
        }
    }
}