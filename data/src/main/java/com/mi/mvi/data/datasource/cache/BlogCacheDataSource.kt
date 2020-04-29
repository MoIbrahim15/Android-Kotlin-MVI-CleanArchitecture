package com.mi.mvi.data.datasource.cache

import com.mi.mvi.data.entity.BlogPostEntity

interface BlogCacheDataSource {

    companion object {
        const val PAGINATION_PAGE_SIZE = 10

        const val BLOG_FILTER_USERNAME = "username"
        const val BLOG_FILTER_DATE_UPDATED = "date_updated"
        const val BLOG_ORDER_ASC: String = ""
        const val BLOG_ORDER_DESC: String = "-"

        const val ORDER_BY_ASC_DATE_UPDATED = BLOG_ORDER_ASC + BLOG_FILTER_DATE_UPDATED
        const val ORDER_BY_DESC_DATE_UPDATED = BLOG_ORDER_DESC + BLOG_FILTER_DATE_UPDATED
        const val ORDER_BY_ASC_USERNAME = BLOG_ORDER_ASC + BLOG_FILTER_USERNAME
        const val ORDER_BY_DESC_USERNAME = BLOG_ORDER_DESC + BLOG_FILTER_USERNAME
    }
    suspend fun insert(blogPostEntity: BlogPostEntity): Long

    suspend fun deleteBlogPost(blogPostEntity: BlogPostEntity)

    suspend fun updateBlogPost(pk: Int, title: String?, body: String?, image: String?)

    suspend fun getAllBlogPosts(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>

    suspend fun searchBlogPostsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>

    suspend fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>

    suspend fun searchBlogPostsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>

    suspend fun searchBlogPostsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>

    fun saveFilterOptions(filter: String, order: String)

    fun getFilter(): String?

    fun getOrder(): String?
}
