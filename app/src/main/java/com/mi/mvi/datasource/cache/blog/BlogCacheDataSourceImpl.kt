package com.mi.mvi.datasource.cache.blog

import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.datasource.model.BlogPost

class BlogCacheDataSourceImpl(
    private val blogPostDao: BlogPostDao
) : BlogCacheDataSource {

    override suspend fun insert(blogPost: BlogPost): Long {
        return blogPostDao.insert(blogPost)
    }

    override suspend fun deleteBlogPost(blogPost: BlogPost) {
        return blogPostDao.deleteBlogPost(blogPost)
    }

    override suspend fun updateBlogPost(pk: Int, title: String, body: String, image: String) {
        return blogPostDao.updateBlogPost(pk, title, body, image)
    }

    override suspend fun getAllBlogPosts(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPost> {
        return blogPostDao.getAllBlogPosts(query, page, pageSize)
    }

    override suspend fun searchBlogPostsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPost> {
        return blogPostDao.searchBlogPostsOrderByDateDESC(query, page, pageSize)
    }

    override suspend fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPost> {
        return blogPostDao.searchBlogPostsOrderByDateASC(query, page, pageSize)

    }

    override suspend fun searchBlogPostsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPost> {
        return blogPostDao.searchBlogPostsOrderByAuthorDESC(query, page, pageSize)
    }

    override suspend fun searchBlogPostsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPost> {
        return blogPostDao.searchBlogPostsOrderByAuthorASC(query, page, pageSize)
    }
}