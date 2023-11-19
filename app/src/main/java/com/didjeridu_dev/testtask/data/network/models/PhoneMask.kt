package com.didjeridu_dev.testtask.data.network.models

import com.didjeridu_dev.testtask.domain.models.PhoneMaskDomain
import kotlinx.serialization.Serializable

@Serializable
data class PhoneMask(
    val phoneMask: String
){
    fun castToDomain() = PhoneMaskDomain(
        phoneMask = phoneMask
    )
}
