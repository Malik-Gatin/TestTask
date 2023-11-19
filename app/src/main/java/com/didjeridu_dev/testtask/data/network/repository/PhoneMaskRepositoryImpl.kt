package com.didjeridu_dev.testtask.data.network.repository

import com.didjeridu_dev.testtask.data.network.api.PhoneMaskApiService
import com.didjeridu_dev.testtask.domain.models.PhoneMaskDomain
import com.didjeridu_dev.testtask.domain.repository.PhoneMaskRepository
import javax.inject.Inject

class PhoneMaskRepositoryImpl @Inject constructor(
    private val phoneMaskApiService: PhoneMaskApiService
): PhoneMaskRepository {
    override suspend fun getPhoneMask(): PhoneMaskDomain {
        val phoneMask = phoneMaskApiService.getPhoneMask()
        return phoneMask.castToDomain()
    }
}