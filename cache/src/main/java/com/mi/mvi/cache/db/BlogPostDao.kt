package com.mi.mvi.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mi.mvi.cache.model.CachedBlogPost
import com.mi.mvi.data.datasource.cache.BlogCacheDataSource.Companion.PAGINATION_PAGE_SIZE

@Dao
interface BlogPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cachedBlogPost: CachedBlogPost): Long

    @Query(
        """
            DELETE FROM blog_post WHERE pk = :pk
            """
    )
    suspend fun deleteBlogPost(pk: Int)

    @Query(
        """
        UPDATE blog_post SET title = :title, body = :body, image = :image 
        WHERE pk = :pk
        """
    )
    suspend fun updateBlogPost(pk: Int, title: String?, body: String?, image: String?)

    @Query(
        """
        SELECT * FROM blog_post
        WHERE title LIKE '%' || :query || '%'
        OR body LIKE '%' || :query || '%'
        OR username LIKE '%' || :query || '%'
        LIMIT (:page * :pageSize)
        """
    )
    suspend fun getAllBlogPosts(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<CachedBlogPost>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY date_updated DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchBlogPostsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<CachedBlogPost>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY date_updated  ASC LIMIT (:page * :pageSize)"""
    )
    suspend fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<CachedBlogPost>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY username DESC LIMIT (:page * :pageSize)"""
    )
    suspend fun searchBlogPostsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<CachedBlogPost>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY username  ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchBlogPostsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): MutableList<CachedBlogPost>
}
