package com.mi.mvi.cache.source

import android.content.SharedPreferences
import com.mi.mvi.cache.db.BlogPostDao
import com.mi.mvi.cache.mapper.BlogPostEntityMapper
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource.Companion.BLOG_FILTER_DATE_UPDATED
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource.Companion.BLOG_ORDER_ASC
import com.mi.mvi.data.entity.BlogPostEntity

const val BLOG_FILTER: String = "com.mi.mvi.BLOG_FILTER"
const val BLOG_ORDER: String = "com.mi.mvi.BLOG_ORDER"


class BlogCacheDataSourceImpl(
    private val blogPostDao: BlogPostDao,
    private val blogPostEntityMapper: BlogPostEntityMapper,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : BlogCacheDataSource {

    override suspend fun insert(blogPostEntity: BlogPostEntity): Long {
        return blogPostDao.insert(blogPostEntityMapper.mapToCached(blogPostEntity))
    }

    override suspend fun deleteBlogPost(blogPostEntity: BlogPostEntity) {
        return blogPostDao.deleteBlogPost(blogPostEntityMapper.mapToCached(blogPostEntity))
    }

    override suspend fun updateBlogPost(pk: Int, title: String?, body: String?, image: String?) {
        return blogPostDao.updateBlogPost(pk, title, body, image)
    }

    override suspend fun getAllBlogPosts(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.getAllBlogPosts(query, page, pageSize)
            .map { cachedBlogPost ->
                blogPostEntityMapper.mapFromCached(cachedBlogPost)
            }.toMutableList()
    }

    override suspend fun searchBlogPostsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.searchBlogPostsOrderByDateDESC(query, page, pageSize)
            .map { cachedBlogPost ->
                blogPostEntityMapper.mapFromCached(cachedBlogPost)
            }.toMutableList()
    }

    override suspend fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.searchBlogPostsOrderByDateASC(query, page, pageSize)
            .map { cachedBlogPost ->
                blogPostEntityMapper.mapFromCached(cachedBlogPost)
            }.toMutableList()
    }

    override suspend fun searchBlogPostsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.searchBlogPostsOrderByAuthorDESC(query, page, pageSize)
            .map { cachedBlogPost ->
                blogPostEntityMapper.mapFromCached(cachedBlogPost)
            }.toMutableList()
    }

    override suspend fun searchBlogPostsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int
    ): MutableList<BlogPostEntity> {
        return blogPostDao.searchBlogPostsOrderByAuthorASC(query, page, pageSize)
            .map { cachedBlogPost ->
                blogPostEntityMapper.mapFromCached(cachedBlogPost)
            }.toMutableList()
    }


    override fun saveFilterOptions(filter: String, order: String) {
        editor.putString(BLOG_FILTER, filter)
        editor.apply()

        editor.putString(BLOG_ORDER, order)
        editor.apply()
    }

    override fun getFilter(): String? {
        return sharedPreferences.getString(
            BLOG_FILTER,
            BLOG_FILTER_DATE_UPDATED
        )
    }

    override fun getOrder(): String? {
        return sharedPreferences.getString(
            BLOG_ORDER,
            BLOG_ORDER_ASC
        )
    }
}
