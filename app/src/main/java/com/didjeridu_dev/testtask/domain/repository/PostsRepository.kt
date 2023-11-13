package com.didjeridu_dev.testtask.domain.repository

import com.didjeridu_dev.testtask.data.network.models.Post

interface PostsRepository {
    suspend fun getPosts():List<Post>
    suspend fun getPostsFromLocalFile():List<Post>
    suspend fun savePosts(posts: List<Post>)
}