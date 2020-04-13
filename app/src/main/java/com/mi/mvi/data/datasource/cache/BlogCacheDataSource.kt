package com.mi.mvi.data.datasource.cache

import com.mi.mvi.datasource.model.BlogPost
import com.mi.mvi.utils.Constants

interface BlogCacheDataSource {

    suspend fun insert(blogPost: BlogPost): Long

    suspend fun deleteBlogPost(blogPost: BlogPost)

    suspend fun updateBlogPost(pk: Int, title: String, body: String, image: String)

    suspend fun getAllBlogPosts(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPost>

    suspend fun searchBlogPostsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPost>

    suspend fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPost>


    suspend fun searchBlogPostsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPost>

    suspend fun searchBlogPostsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): MutableList<BlogPost>
}