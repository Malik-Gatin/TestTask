package com.didjeridu_dev.testtask.domain.repository

import com.didjeridu_dev.testtask.domain.models.PhoneMaskDomain

interface PhoneMaskRepository {
    suspend fun getPhoneMask(): PhoneMaskDomain
}