package com.mi.mvi.features.main.blog

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
import com.mi.mvi.domain.datastate.DataState
import com.mi.mvi.domain.model.BlogPostView
import com.mi.mvi.common.TopSpacingItemDecoration
import com.mi.mvi.features.main.blog.viewmodel.*
import com.mi.mvi.utils.Constants.Companion.isPaginationDone
import kotlinx.android.synthetic.main.fragment_blog.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


const val BLOG_FILTER_USERNAME = "username"
const val BLOG_FILTER_DATE_UPDATED = "date_updated"
const val BLOG_ORDER_ASC: String = ""
const val BLOG_ORDER_DESC: String = "-"

@FlowPreview
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

            when (dataState) {
                is DataState.SUCCESS -> {
                    dataState.data?.let {
                        viewModel.handleIncomingBlogListData(it)
                    }
                }
                is DataState.ERROR -> {
                    if (isPaginationDone(dataState.stateMessage?.message)) {
                        viewModel.setQueryExhausted(true)
                    } else {
                        dataStateChangeListener?.onDataStateChangeListener(dataState)
                    }
                }
                is DataState.LOADING -> {
                    dataStateChangeListener?.onDataStateChangeListener(dataState)

                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                recyclerAdapter.apply {
                    submitList(
                        list = viewState.blogFields.blogList?.map { blogPostMapper.mapToView(it) }
                            ?.toMutableList(),
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

    override fun onItemSelected(item: BlogPostView) {
        viewModel.setBlogPost(item)
        findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
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

                val blogListFilter =
                    if (selectedFilter.text.toString() == getString(R.string.filter_author)) {
                        BLOG_FILTER_USERNAME
                    } else BLOG_FILTER_DATE_UPDATED

                val blogListOrder =
                    if (selectedOrder.text.toString() == getString(R.string.filter_desc)) {
                        BLOG_ORDER_DESC
                    } else BLOG_ORDER_ASC

                viewModel.saveFilterOptions(blogListFilter, blogListOrder)
                viewModel.setBlogFilter(blogListFilter)
                viewModel.setBlogOrder(blogListOrder)

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
