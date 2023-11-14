package com.didjeridu_dev.testtask.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didjeridu_dev.testtask.data.network.models.Post
import com.didjeridu_dev.testtask.domain.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

enum class PostsApiStatus {LOADING, ERROR, DONE}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postsRepository: PostsRepository
):ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    private val _status = MutableLiveData<PostsApiStatus>()
    val posts:LiveData<List<Post>>
        get() = _posts
    val status:LiveData<PostsApiStatus>
        get() = _status

    init {
        getPosts()
    }

    fun getPosts(){
        viewModelScope.launch {
            _status.value = PostsApiStatus.LOADING
            try{
                _posts.value = postsRepository.getPosts().map{
                    it.copy(date = formatDate(it.date))
                }
                _posts.value?.let{postsRepository.savePosts(it)}
                _status.value = PostsApiStatus.DONE
            } catch (e: Exception){
                _posts.value = postsRepository.getPostsFromLocalFile()
                _status.value = PostsApiStatus.ERROR
            }
        }
    }

    private fun formatDate(inputDate: String): String{
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormatter = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
        return try {
            val date = inputFormatter.parse(inputDate)
            date?.let { outputFormatter.format(it) } ?: inputDate
        } catch (e: Exception){
            e.printStackTrace()
            inputDate
        }
    }

    fun sortByDate(){
        val sdf = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
        val sortedPosts = _posts.value?.sortedByDescending {post ->
            sdf.parse(post.date)
        }
        sortedPosts?.let {
            _posts.value = it
        }
    }

    fun sortByServer(){
        val sortedPosts = _posts.value?.sortedBy {post ->
            post.sort
        }
        sortedPosts?.let {
            _posts.value = it
        }
    }
}