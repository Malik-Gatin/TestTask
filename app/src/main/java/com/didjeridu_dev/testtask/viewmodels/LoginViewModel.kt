package com.didjeridu_dev.testtask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didjeridu_dev.testtask.data.network.models.Authentication
import com.didjeridu_dev.testtask.data.network.models.Login
import com.didjeridu_dev.testtask.data.network.models.PhoneMask
import com.didjeridu_dev.testtask.domain.repository.AuthenticationRepository
import com.didjeridu_dev.testtask.domain.repository.PhoneMaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody
import javax.inject.Inject

enum class AuthApiStatus {LOADING, ERROR, DONE}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val phoneMaskRepository: PhoneMaskRepository,
    private val authenticationRepository: AuthenticationRepository
):ViewModel() {
    private val _phoneMask = MutableLiveData<PhoneMask>()
    private val _status = MutableLiveData<AuthApiStatus>()
    private val _responseAuth = MutableLiveData<Authentication>()
    val phoneMask: LiveData<PhoneMask>
        get() = _phoneMask
    val status:LiveData<AuthApiStatus>
        get() = _status
    val responseAuth: LiveData<Authentication>
        get() = _responseAuth

    init{
        getPhoneMask()
    }
    private fun getPhoneMask(){
        viewModelScope.launch {
            _phoneMask.value = phoneMaskRepository.getPhoneMask()
        }
    }

    fun auth(login: Login){
        viewModelScope.launch {
            _status.value = AuthApiStatus.LOADING
            try{
                val requestBody = createFormBody(login)
                val response = authenticationRepository.auth(requestBody)
                when (response.raw().code){
                    200 -> {
                        _responseAuth.value = response.body()
                        _status.value = AuthApiStatus.DONE
                    }
                    else -> {
                        _responseAuth.value = Authentication(isSuccess = false)
                        _status.value = AuthApiStatus.ERROR
                    }
                }
            }catch (e: Exception) {
                _responseAuth.value = Authentication(isSuccess = false)
                _status.value = AuthApiStatus.ERROR
            }
        }
    }

    private fun createFormBody(loginData: Login): RequestBody {
        return FormBody.Builder()
            .add("phone", loginData.phone)
            .add("password", loginData.password)
            .build()
    }
}