package com.didjeridu_dev.testtask.data.network.api

import com.didjeridu_dev.testtask.data.network.models.Post
import retrofit2.http.GET

interface PostsApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}