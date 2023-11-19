package com.didjeridu_dev.testtask.domain.repository

import com.didjeridu_dev.testtask.domain.models.LocalPostData
import com.didjeridu_dev.testtask.domain.models.PostDomain

interface PostsRepository {
    suspend fun getPosts():List<PostDomain>
    suspend fun getPostsFromLocalFile():LocalPostData?
    suspend fun savePosts(localPostData: LocalPostData)
}