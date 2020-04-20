package com.mi.mvi.presentation.main.blog

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.mi.mvi.R
import com.mi.mvi.cache.entity.BlogPostEntity
import com.mi.mvi.presentation.common.TopSpacingItemDecoration
import com.mi.mvi.presentation.main.blog.state.BlogViewState
import com.mi.mvi.presentation.main.blog.viewmodel.*
import com.mi.mvi.utils.Constants.Companion.BLOG_FILTER_DATE_UPDATED
import com.mi.mvi.utils.Constants.Companion.BLOG_FILTER_USERNAME
import com.mi.mvi.utils.Constants.Companion.BLOG_ORDER_ASC
import com.mi.mvi.utils.Constants.Companion.INVALID_PAGE_NUMBER
import com.mi.mvi.utils.Constants.Companion.isPaginationDone
import com.mi.mvi.utils.response_handler.DataState
import kotlinx.android.synthetic.main.fragment_blog.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class BlogFragment : BaseBlogFragment(R.layout.fragment_blog),
    BlogListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var recyclerAdapter: BlogListAdapter
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        swipe_refresh.setOnRefreshListener(this)
        initRecyclerView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFromCache()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        blog_post_recyclerview.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    private fun onBlogSearchOrFilter() {
        viewModel.loadFirstPage()
        resetUI()
    }

    private fun resetUI() {
        blog_post_recyclerview.smoothScrollToPosition(0)
        uiCommunicationListener?.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.let {
                dataStateChangeListener?.onDataStateChangeListener(dataState)

                if (isPaginationDone(dataState.stateMessage?.message)) {
                    viewModel.setQueryExhausted(true)
                } else {
                    handlePagination(dataState)
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                recyclerAdapter.apply {
                    submitList(
                        list = viewState.blogFields.blogListEntity,
                        isQueryExhausted = viewState.blogFields.isQueryExhausted ?: false
                    )
                }
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

        // case 1 : ENTER ON ARROW
        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        searchPlate.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED ||
                actionId == EditorInfo.IME_ACTION_SEARCH
            ) {
                val searchQuery = v.text.toString()
                viewModel.setQuery(searchQuery)
                onBlogSearchOrFilter()
            }
            true
        }
        // cas2: SEARCH BUTTON CLICKED (in toolbar)
        (searchView.findViewById(R.id.search_go_btn) as View).setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            viewModel.setQuery(searchQuery)
            onBlogSearchOrFilter()
        }
    }

    private fun handlePagination(dataState: DataState<BlogViewState>?) {
        // handle incomming data from data state
        dataState?.data?.let { viewState ->
            viewModel.handleIncomingBlogListData(viewState)
        }

        // check for pagination end
        dataState?.stateMessage?.let { stateMessage ->
            if (stateMessage.message == INVALID_PAGE_NUMBER) {
                // set query exhausted to update RecyclerView with
                // "No more results..." list item
                viewModel.setQueryExhausted(true)
            }
        }
    }

    private fun initRecyclerView() {
        blog_post_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@BlogFragment.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(30)
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
                        viewModel.nextPage()
                    }
                }
            })

            adapter = recyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        blog_post_recyclerview.adapter = null // clean references for memory leaks
    }

    override fun onItemSelected(position: Int, item: BlogPostEntity) {
        viewModel.setBlogPost(item)
        findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.blogFields?.layoutManagerState?.let { lmState ->
            blog_post_recyclerview?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_settings -> {
                showFilterOptions()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        onBlogSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    private fun showFilterOptions() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_blog_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilter()
            if (filter == BLOG_FILTER_DATE_UPDATED) {
                view.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_date)
            } else {
                view.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_author)
            }

            val order = viewModel.getOrder()

            if (order == BLOG_ORDER_ASC) {
                view.findViewById<RadioGroup>(R.id.order_group).check(R.id.filter_asc)
            } else {
                view.findViewById<RadioGroup>(R.id.order_group).check(R.id.filter_desc)
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                val selectedFilter = view.findViewById<RadioButton>(
                    view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId
                )

                val selectedOrder = view.findViewById<RadioButton>(
                    view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId
                )

                var filter = BLOG_FILTER_DATE_UPDATED
                if (selectedFilter.text.toString() == getString(R.string.filter_author)) {
                    filter = BLOG_FILTER_USERNAME
                }

                var order = ""
                if (selectedOrder.text.toString() == getString(R.string.filter_desc)) {
                    order = "-"
                }
                viewModel.saveFilterOptions(filter, order)
                viewModel.setBlogFilter(filter)
                viewModel.setBlogOrder(order)
                onBlogSearchOrFilter()
                dialog.dismiss()
            }
            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}
