package com.didjeridu_dev.testtask.data

import com.didjeridu_dev.testtask.data.network.PhoneMask
import com.didjeridu_dev.testtask.data.network.PhoneMaskApiService
import javax.inject.Inject

interface PhoneMaskRepository {
    suspend fun getPhoneMask():PhoneMask
}

class NetworkPhoneMaskRepository @Inject constructor(
    private val phoneMaskApiService: PhoneMaskApiService
): PhoneMaskRepository{
    override suspend fun getPhoneMask(): PhoneMask {
        return phoneMaskApiService.getPhoneMask()
    }
}