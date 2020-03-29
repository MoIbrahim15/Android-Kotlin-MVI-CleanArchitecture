package com.mi.mvi.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mi.mvi.R
import com.mi.mvi.data.models.BlogPost
import com.mi.mvi.ui.BaseFragment
import com.mi.mvi.ui.main.blog.state.BlogEventState
import com.mi.mvi.utils.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_blog.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class BlogFragment : BaseFragment(R.layout.fragment_blog), BlogListAdapter.Interaction {

    private val blogViewModel: BlogViewModel by sharedViewModel()

    private lateinit var recyclerAdapter: BlogListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        executeSearch()
        initRecyclerView()
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
                        blogViewModel.setBlogList(it.blogsFields.blogList)
                    }
                }
            }

        })

        blogViewModel.viewState.observe(viewLifecycleOwner, Observer {viewState->
            viewState?.let {
                recyclerAdapter.submitList(
                    list = viewState.blogsFields.blogList,
                    isQueryExhausted = true
                )
            }
        })
    }


    private fun initRecyclerView() {
        blog_post_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@BlogFragment.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingItemDecoration)
            addItemDecoration(topSpacingItemDecoration)

            recyclerAdapter = BlogListAdapter(
                interaction = this@BlogFragment)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d("BlogFragment", "PAGINATION: ")
                        //TODO pagination load next
                    }
                }
            })

            adapter = recyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        blog_post_recyclerview.adapter = null //clean references for memory leaks
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        Log.d("OnItemSelected", "item is $position + $item")
        findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
    }
}