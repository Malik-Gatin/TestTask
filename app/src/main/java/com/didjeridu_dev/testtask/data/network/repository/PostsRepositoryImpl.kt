package com.didjeridu_dev.testtask.data.network.repository

import com.didjeridu_dev.testtask.App.AppConstants.POSTS
import com.didjeridu_dev.testtask.data.local.SharedPreferencesManager
import com.didjeridu_dev.testtask.data.network.api.PostsApiService
import com.didjeridu_dev.testtask.data.network.models.Post
import com.didjeridu_dev.testtask.domain.models.LocalPostData
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
    override suspend fun getPostsFromLocalFile(): LocalPostData? {
        val jsonPosts = sharedPreferencesManager.getData(POSTS)
        return if(jsonPosts != null){
            Gson().fromJson(jsonPosts, object : TypeToken<LocalPostData>() {}.type)
        }else {
            null
        }
    }
    override suspend fun savePosts(localPostData: LocalPostData) {
        val jsonPosts = Gson().toJson(localPostData)
        sharedPreferencesManager.saveData(POSTS, jsonPosts)
    }
}