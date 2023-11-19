package com.didjeridu_dev.testtask

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application(){
    object AppConstants{
        const val BASE_URL = "http://dev-exam.l-tech.ru"
        const val BASE_API_URL = "$BASE_URL/api/v1/"
        const val POSTS = "posts"

        const val DEFAULT_MASK = "+7 (ХХХ) ХХХ-ХХ-ХХ"
        const val SELECTED_ITEM = "selectedItem"

        // частота обновления данных с сервера: каждые n мс
        const val UPDATE_FREQUENCY = 1_200_000L
    }
}
