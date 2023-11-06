package com.didjeridu_dev.testtask.di

import android.util.Log
import com.didjeridu_dev.testtask.data.NetworkPhoneMaskRepository
import com.didjeridu_dev.testtask.data.PhoneMaskRepository
import com.didjeridu_dev.testtask.data.network.PhoneMaskApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://dev-exam.l-tech.ru/api/v1/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val client = OkHttpClient.Builder()
        .addInterceptor{chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if(!response.isSuccessful){
                Log.e("NetworkError", "Error occurred: " + response.code)
            }
            response
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun providePhoneMaskService(retrofit: Retrofit):PhoneMaskApiService{
        return retrofit.create(PhoneMaskApiService::class.java)
    }

    @Provides
    fun providePhoneMaskRepository(phoneMaskApiService: PhoneMaskApiService): PhoneMaskRepository {
        return NetworkPhoneMaskRepository(phoneMaskApiService)
    }

}