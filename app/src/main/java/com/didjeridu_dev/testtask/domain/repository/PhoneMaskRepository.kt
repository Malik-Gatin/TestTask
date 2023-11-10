package com.didjeridu_dev.testtask.domain.repository

import com.didjeridu_dev.testtask.data.network.models.PhoneMask

interface PhoneMaskRepository {
    suspend fun getPhoneMask(): PhoneMask
}