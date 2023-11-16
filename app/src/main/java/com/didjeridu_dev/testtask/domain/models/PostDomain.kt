package com.didjeridu_dev.testtask.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class PostDomain(
    val id: String,
    val title:String,
    val text: String,
    val image: String,
    val sort: Int,
    val date: String
): Parcelable
