package com.mi.mvi.ui.main.blog

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
class BlogFragment : BaseFragment(R.layout.fragment_blog),
    BlogListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {

    private val blogViewModel: BlogViewModel by sharedViewModel()

    private lateinit var recyclerAdapter: BlogListAdapter
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        swipe_refresh.setOnRefreshListener(this)
        initRecyclerView()
        subscribeObservers()
        if (savedInstanceState == null) {
            blogViewModel.loadFirstPage()
        }
    }

    private fun onBlogSearchOrFilter() {
        blogViewModel.loadFirstPage()
        resetUI()
    }

    private fun resetUI() {
        blog_post_recyclerview.smoothScrollToPosition(0)
        dataStateChangeListener?.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun subscribeObservers() {
        blogViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            handlePagination(dataState)
            dataStateChangeListener?.onDataStateChangeListener(dataState)
        })

        blogViewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                if (blogViewModel.getPage() * PAGINATION_PAGE_SIZE > viewState.blogsFields.blogList.size) {
                    viewState.blogsFields.isQueryExhausted = true
                }
                recyclerAdapter.submitList(
                    list = viewState.blogsFields.blogList,
                    isQueryExhausted = viewState.blogsFields.isQueryExhausted
                )
            }
        })
    }


    private fun initSearchView(menu: Menu) {
        activity?.apply {
            val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setIconifiedByDefault(true)
            searchView.isSubmitButtonEnabled = true
        }

        //case 1 : ENTER ON ARROW
        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        searchPlate.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH
            ) {
                val searchQuery = v.text.toString()
                Log.e("", searchQuery)
                blogViewModel.setQuery(searchQuery)
                onBlogSearchOrFilter()
            }
            true
        }
        //cas2: SEARCH BUTTON CLICKED (in toolbar)
        (searchView.findViewById(R.id.search_go_btn) as View).setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e("SEARCH_QUERY", searchQuery)
            blogViewModel.setQuery(searchQuery)
            onBlogSearchOrFilter()
        }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    override fun onRefresh() {
        onBlogSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

}