package com.didjeridu_dev.testtask.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didjeridu_dev.testtask.App.AppConstants.DEFAULT_MASK
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

enum class AuthApiStatus {DEFAULT, LOADING, ERROR, DONE}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val phoneMaskRepository: PhoneMaskRepository,
    private val authenticationRepository: AuthenticationRepository
):ViewModel() {
    private val _phoneMask = MutableLiveData<PhoneMask>(PhoneMask(DEFAULT_MASK))
    private val _status = MutableLiveData<AuthApiStatus>()
    private val _responseAuth = MutableLiveData<Authentication>()
    private val _isEnableButton = MutableLiveData<Boolean>(false)
    private val _isHidePassword = MutableLiveData<Boolean>(true)
    private val _phone = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    val phoneMask: LiveData<PhoneMask>
        get() = _phoneMask
    val status:LiveData<AuthApiStatus>
        get() = _status
    val isEnableButton: LiveData<Boolean>
        get() = _isEnableButton
    val phone:LiveData<String>
        get() = _phone
    val password:LiveData<String>
        get() = _password
    val isHidePassword:LiveData<Boolean>
        get() = _isHidePassword

    var isConnectedError: Boolean = false

    init{
        getPhoneMask()
    }
    private fun getPhoneMask(){
        viewModelScope.launch {
            try{
                _phoneMask.value = phoneMaskRepository.getPhoneMask()
            }catch(e:Exception){
                e.printStackTrace()
            }
            _phoneMask.value?.let {
                val loginData = authenticationRepository.getLoginDataFromLocalFile(it.phoneMask)
                _phone.value = loginData?.phone
                _password.value = loginData?.password
            }
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
                        _phoneMask.value?.let{phoneMask->
                            authenticationRepository.savePhoneAndPassword(phoneMask.phoneMask,
                                Login(_phone.value!!, _password.value!!)
                            )
                        }
                        _status.value = AuthApiStatus.DONE
                    }
                    else -> {
                        _responseAuth.value = Authentication(isSuccess = false)
                        _status.value = AuthApiStatus.ERROR
                    }
                }
            }catch (e: Exception) {
                _responseAuth.value = Authentication(isSuccess = false)
                isConnectedError = true
                _status.value = AuthApiStatus.DEFAULT
            }
        }
    }

    fun resetStatus(){
        _status.value = AuthApiStatus.DEFAULT
        isConnectedError = false
    }

    private fun createFormBody(loginData: Login): RequestBody {
        return FormBody.Builder()
            .add("phone", loginData.phone)
            .add("password", loginData.password)
            .build()
    }

    fun setPhone(phone:String){
        _phone.value = phone
        setEnabled()
    }
    fun setPassword(password:String){
        _password.value = password
        setEnabled()
    }
    fun changeVisiblePassword(){
        _isHidePassword.value = _isHidePassword.value?.not()
    }

    fun setEnabled(isEnable:Boolean? = null) {
        when (isEnable) {
            true -> {
                if(!_phone.value.isNullOrEmpty() && !_password.value.isNullOrEmpty())
                    _isEnableButton.value = true
            }
            false -> {
                _isEnableButton.value = false
            }
            else -> {
                _isEnableButton.value =
                    !_phone.value.isNullOrEmpty() && !_password.value.isNullOrEmpty()
                            && _phone.value?.length == _phoneMask.value?.phoneMask?.length
            }
        }
    }
}