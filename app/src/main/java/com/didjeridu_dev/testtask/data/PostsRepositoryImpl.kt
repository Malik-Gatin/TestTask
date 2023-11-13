package com.didjeridu_dev.testtask.data

import com.didjeridu_dev.testtask.data.network.PostsApiService
import com.didjeridu_dev.testtask.data.network.models.Post
import com.didjeridu_dev.testtask.domain.repository.PostsRepository
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val postsApiService: PostsApiService
):PostsRepository {
    override suspend fun getPosts(): List<Post> {
        return postsApiService.getPosts()
    }
}