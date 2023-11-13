package com.didjeridu_dev.testtask.data

import com.didjeridu_dev.testtask.App.AppConstants.POSTS
import com.didjeridu_dev.testtask.data.local.SharedPreferencesManager
import com.didjeridu_dev.testtask.data.network.PostsApiService
import com.didjeridu_dev.testtask.data.network.models.Post
import com.didjeridu_dev.testtask.domain.repository.PostsRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val postsApiService: PostsApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
):PostsRepository {
    override suspend fun getPosts(): List<Post> {
        return postsApiService.getPosts()
    }
    override suspend fun getPostsFromLocalFile(): List<Post> {
        val jsonPosts = sharedPreferencesManager.getData(POSTS)
        return if(jsonPosts != null){
            Gson().fromJson(jsonPosts, object : TypeToken<List<Post>>() {}.type)
        }else {
            listOf()
        }
    }
    override suspend fun savePosts(posts: List<Post>) {
        val jsonPosts = Gson().toJson(posts)
        sharedPreferencesManager.saveData(POSTS, jsonPosts)
    }
}