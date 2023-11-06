package com.didjeridu_dev.testtask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didjeridu_dev.testtask.data.PhoneMaskRepository
import com.didjeridu_dev.testtask.data.network.PhoneMask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val phoneMaskRepository: PhoneMaskRepository
):ViewModel() {
    private val _phoneMask = MutableLiveData<PhoneMask>()
    val phoneMask = _phoneMask

    init{
        getPhoneMask()
    }
    private fun getPhoneMask(){
        viewModelScope.launch {
            _phoneMask.value = phoneMaskRepository.getPhoneMask()
        }
    }
}