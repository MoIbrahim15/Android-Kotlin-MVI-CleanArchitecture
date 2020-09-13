package com.mi.mvi.features.main.blog

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.mi.mvi.R
import com.mi.mvi.features.main.blog.viewmodel.BlogListItem
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

class BlogViewHolder(
    itemView: View
) : RecyclerViewHolder<BlogListItem.Item>(itemView) {

    override fun bind(position: Int, item: BlogListItem.Item) {
        val blogPostView = item.blogPostView
        super.bind(position, item)
        Glide.with(itemView)
            .load(blogPostView.image)
            .placeholder(R.drawable.default_image)
            .transition(withCrossFade())
            .into(itemView.imgBlog)

        itemView.tvBlogTitle.text = blogPostView.title
        itemView.tvBlogAuthor.text = blogPostView.username
        itemView.tvBlogDate.text = blogPostView.date_updated
    }
}