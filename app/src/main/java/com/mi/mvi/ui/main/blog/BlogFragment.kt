package com.mi.mvi.ui.main.blog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mi.mvi.R
import com.mi.mvi.ui.BaseFragment
import com.mi.mvi.ui.main.blog.state.BlogEventState
import kotlinx.android.synthetic.main.fragment_blog.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class BlogFragment : BaseFragment(R.layout.fragment_blog) {

    private val blogViewModel: BlogViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goViewBlogFragment.setOnClickListener {
            findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
        }

        subscribeObservers()
        executeSearch()
    }


    private fun executeSearch() {
        blogViewModel.setQuery("")
        blogViewModel.setStateEvent(BlogEventState.BlogSearchEvent())
    }

    private fun subscribeObservers() {
        blogViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataStateChangeListener?.onDataStateChangeListener(dataState)
            dataState?.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        blogViewModel.setBlogList(it.blogsFields.blogs)
                    }
                }
            }

        })

        blogViewModel.viewState.observe(viewLifecycleOwner, Observer {

        })
    }
}