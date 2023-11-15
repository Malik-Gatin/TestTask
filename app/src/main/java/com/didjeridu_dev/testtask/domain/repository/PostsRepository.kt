package com.didjeridu_dev.testtask.domain.repository

import com.didjeridu_dev.testtask.data.network.models.Post
import com.didjeridu_dev.testtask.domain.models.LocalPostData

interface PostsRepository {
    suspend fun getPosts():List<Post>
    suspend fun getPostsFromLocalFile():LocalPostData?
    suspend fun savePosts(localPostData: LocalPostData)
}