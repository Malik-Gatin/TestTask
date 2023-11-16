package com.didjeridu_dev.testtask.data.network.repository

import com.didjeridu_dev.testtask.data.network.models.PhoneMask
import com.didjeridu_dev.testtask.data.network.api.PhoneMaskApiService
import com.didjeridu_dev.testtask.domain.repository.PhoneMaskRepository
import javax.inject.Inject

class PhoneMaskRepositoryImpl @Inject constructor(
    private val phoneMaskApiService: PhoneMaskApiService
): PhoneMaskRepository {
    override suspend fun getPhoneMask(): PhoneMask {
        return phoneMaskApiService.getPhoneMask()
    }
}