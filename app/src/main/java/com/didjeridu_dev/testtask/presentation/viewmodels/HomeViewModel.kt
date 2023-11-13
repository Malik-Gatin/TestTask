package com.didjeridu_dev.testtask.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didjeridu_dev.testtask.data.network.models.Post
import com.didjeridu_dev.testtask.domain.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
                _posts.value = postsRepository.getPosts()
                _posts.value?.let{postsRepository.savePosts(it)}
                _status.value = PostsApiStatus.DONE
            } catch (e: Exception){
                _status.value = PostsApiStatus.ERROR
                _posts.value = postsRepository.getPosts()
            }
        }
    }


}