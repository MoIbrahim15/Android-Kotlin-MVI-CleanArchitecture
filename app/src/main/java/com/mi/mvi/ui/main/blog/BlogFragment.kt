package com.mi.mvi.ui.main.blog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mi.mvi.R
import com.mi.mvi.data.models.BlogPost
import com.mi.mvi.data.response_handler.DataState
import com.mi.mvi.ui.BaseFragment
import com.mi.mvi.ui.main.blog.state.BlogViewState
import com.mi.mvi.ui.main.blog.viewmodel.*
import com.mi.mvi.utils.Constants.Companion.PAGINATION_PAGE_SIZE
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
        initRecyclerView()
        subscribeObservers()
        if (savedInstanceState == null) {
            blogViewModel.loadFirstPage()
        }
    }


    private fun subscribeObservers() {
        blogViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            handlePagination(dataState)
            dataStateChangeListener?.onDataStateChangeListener(dataState)
        })

        blogViewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                if (blogViewModel.getPage() * PAGINATION_PAGE_SIZE > viewState.blogsFields.blogList.size){
                    viewState.blogsFields.isQueryExhausted = true
                }
                recyclerAdapter.submitList(
                    list = viewState.blogsFields.blogList,
                    isQueryExhausted = viewState.blogsFields.isQueryExhausted
                )
            }
        })
    }

    private fun handlePagination(dataState: DataState<BlogViewState>?) {
        //handle incomming data from data state
        dataState?.data?.let { data ->
            data.data?.let { event ->
                event.getContentIfNotHandled()?.let {
                    blogViewModel.handleIncomingBlogListData(it)
                }
            }
        }

        //check for pagination end
        dataState?.error?.let { event ->
            event.peekContent().response?.messageRes?.let {
                if (it == R.string.invalid_page) {
                    // handle the error message event so it doesn't display in UI
                    event.getContentIfNotHandled()

                    // set query exhausted to update RecyclerView with
                    // "No more results..." list item
                    blogViewModel.setQueryExhausted(true)
                }
            }
        }
    }


    private fun initRecyclerView() {
        blog_post_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@BlogFragment.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingItemDecoration)
            addItemDecoration(topSpacingItemDecoration)

            recyclerAdapter = BlogListAdapter(
                interaction = this@BlogFragment
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        blogViewModel.nextPage()
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
        blogViewModel.setBlogPost(item)
        findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
    }
}