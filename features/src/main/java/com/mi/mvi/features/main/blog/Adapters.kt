package com.mi.mvi.features.main.blog

import com.mi.mvi.R
import com.mi.mvi.domain.model.BlogPostView
import com.mi.mvi.features.main.blog.viewmodel.BlogListItem
import me.ibrahimyilmaz.kiel.adapterOf

internal fun createBlogListAdapter(onItemSelected: (BlogPostView) -> Unit) =
    adapterOf<BlogListItem> {
        diff(
            areItemsTheSame = { old, new ->
                when {
                    old is BlogListItem.Item && new is BlogListItem.Item
                    -> old.blogPostView.pk == new.blogPostView.pk
                    else -> old === new
                }
            },
            areContentsTheSame = { old, new -> old == new }
        )
        register(
            layoutResource = R.layout.layout_no_more_results,
            viewHolder = ::GenericViewHolder
        )
        register(
            layoutResource = R.layout.layout_blog_list_item,
            viewHolder = ::BlogViewHolder,
            onBindViewHolder = { viewHolder, _, item ->
                viewHolder.itemView.setOnClickListener {
                    onItemSelected(item.blogPostView)
                }
            }
        )
    }