package com.didjeridu_dev.testtask.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didjeridu_dev.testtask.App.AppConstants.UPDATE_FREQUENCY
import com.didjeridu_dev.testtask.data.network.models.Post
import com.didjeridu_dev.testtask.domain.models.LocalPostData
import com.didjeridu_dev.testtask.domain.repository.PostsRepository
import com.didjeridu_dev.testtask.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

enum class PostsApiStatus {LOADING, ERROR, DONE}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postsRepository: PostsRepository
):ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    private val _status = MutableLiveData<PostsApiStatus>()
    private val _executor = Executors.newSingleThreadScheduledExecutor()
    val posts:LiveData<List<Post>>
        get() = _posts
    val status:LiveData<PostsApiStatus>
        get() = _status

    init {
        updateDataFromServerEveryHour()
    }

    /**
     Периодическое обновление данных с сервера
     */
    private fun updateDataFromServerEveryHour(){
        _executor.scheduleAtFixedRate({getPosts()}, 0, UPDATE_FREQUENCY, TimeUnit.MILLISECONDS)
    }

    /**
    Получаем текущую дату и время
     */
    private fun getCurrentDate(): Long{
        return System.currentTimeMillis()
    }

    /**
     Получаем список постов из локали или с сервера
     */
    private fun getPosts(){
        val currentDate = getCurrentDate()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                postsRepository.getPostsFromLocalFile()?.let {
                    val difference = currentDate - it.date
                    if(difference < UPDATE_FREQUENCY) {
                        _posts.postValue(it.posts)
                    }else{
                        getPostsFromServer()
                        _status.value = PostsApiStatus.DONE
                    }
                } ?: getPostsFromServer()
            }
        }
    }

    /**
    Получаем список постов с сервера
     */
    fun getPostsFromServer(){
        viewModelScope.launch {
            _status.value = PostsApiStatus.LOADING
            try{
                _posts.value = postsRepository.getPosts().map{
                    it.copy(date = DateUtils.formatDate(it.date))
                }
                _posts.value?.let{
                    val localPostData = LocalPostData(getCurrentDate(), it)
                    postsRepository.savePosts(localPostData)
                }
                _status.value = PostsApiStatus.DONE
            } catch (e: Exception){
                _status.value = PostsApiStatus.ERROR
            }
        }
    }

    /**
     Сортировка по дате
     */
    fun sortByDate(){
        val sdf = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
        val sortedPosts = _posts.value?.sortedByDescending {post ->
            sdf.parse(post.date)
        }
        sortedPosts?.let {
            _posts.value = it
        }
    }

    /**
    Сортировка по умолчанию (по данным с сервера)
     */
    fun sortByServer(){
        val sortedPosts = _posts.value?.sortedBy {post ->
            post.sort
        }
        sortedPosts?.let {
            _posts.value = it
        }
    }
}