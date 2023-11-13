package com.didjeridu_dev.testtask

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application(){
    object AppConstants{
        const val BASE_URL = "http://dev-exam.l-tech.ru"
        const val BASE_API_URL = "$BASE_URL/api/v1/"
        const val POSTS = "posts"
    }
}