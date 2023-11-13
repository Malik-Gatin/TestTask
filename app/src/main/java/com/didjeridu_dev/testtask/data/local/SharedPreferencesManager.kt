package com.didjeridu_dev.testtask.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    suspend fun saveData(key: String, value: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    suspend fun getData(key: String): String? = withContext(Dispatchers.IO){
        return@withContext sharedPreferences.getString(key, null)
    }
}