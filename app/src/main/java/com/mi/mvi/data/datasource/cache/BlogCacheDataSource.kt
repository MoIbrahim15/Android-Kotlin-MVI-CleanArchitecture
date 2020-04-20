package com.mi.mvi.data.datasource.cache

import com.mi.mvi.cache.entity.BlogPostEntity
import com.mi.mvi.utils.Constants

interface BlogCacheDataSource {

    suspend fun insert(blogPostEntity: BlogPostEntity): Long

    suspend fun deleteBlogPost(blogPostEntity: BlogPostEntity)

    suspend fun updateBlogPost(pk: Int, title: String, body: String, image: String)

    suspend fun getAllBlogPosts(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>

    suspend fun searchBlogPostsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>

    suspend fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>

    suspend fun searchBlogPostsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>

    suspend fun searchBlogPostsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPostEntity>
}
