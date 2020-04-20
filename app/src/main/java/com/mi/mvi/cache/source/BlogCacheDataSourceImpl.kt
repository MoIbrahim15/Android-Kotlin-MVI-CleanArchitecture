package com.mi.mvi.cache.source

import com.mi.mvi.cache.db.BlogPostDao
import com.mi.mvi.cache.entity.BlogPostEntity
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource

class BlogCacheDataSourceImpl(
    private val blogPostDao: BlogPostDao
) : BlogCacheDataSource {

    override suspend fun insert(blogPostEntity: BlogPostEntity): Long {
        return blogPostDao.insert(blogPostEntity)
    }

    override suspend fun deleteBlogPost(blogPostEntity: BlogPostEntity) {
        return blogPostDao.deleteBlogPost(blogPostEntity)
    }

    override suspend fun updateBlogPost(pk: Int, title: String, body: String, image: String) {
        return blogPostDao.updateBlogPost(pk, title, body, image)
    }

    override suspend fun getAllBlogPosts(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.getAllBlogPosts(query, page, pageSize)
    }

    override suspend fun searchBlogPostsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.searchBlogPostsOrderByDateDESC(query, page, pageSize)
    }

    override suspend fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.searchBlogPostsOrderByDateASC(query, page, pageSize)
    }

    override suspend fun searchBlogPostsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.searchBlogPostsOrderByAuthorDESC(query, page, pageSize)
    }

    override suspend fun searchBlogPostsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.searchBlogPostsOrderByAuthorASC(query, page, pageSize)
    }
}
