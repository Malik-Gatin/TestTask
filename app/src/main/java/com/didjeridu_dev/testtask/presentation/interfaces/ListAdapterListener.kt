package com.didjeridu_dev.testtask.presentation.interfaces

import com.didjeridu_dev.testtask.domain.models.PostDomain

interface ListAdapterListener {
    fun onItemClickListener(post: PostDomain)
}