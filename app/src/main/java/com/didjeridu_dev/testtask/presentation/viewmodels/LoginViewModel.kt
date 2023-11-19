package com.didjeridu_dev.testtask.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didjeridu_dev.testtask.App.AppConstants.DEFAULT_MASK
import com.didjeridu_dev.testtask.domain.models.Login
import com.didjeridu_dev.testtask.domain.models.AuthenticationDomain
import com.didjeridu_dev.testtask.domain.models.PhoneMaskDomain
import com.didjeridu_dev.testtask.domain.repository.AuthenticationRepository
import com.didjeridu_dev.testtask.domain.repository.PhoneMaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/**
 * enum класс статусов запроса на сервер
 */
enum class AuthApiStatus {DEFAULT, LOADING, ERROR, DONE}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val phoneMaskRepository: PhoneMaskRepository,
    private val authenticationRepository: AuthenticationRepository
):ViewModel() {
    private val _phoneMask = MutableLiveData(PhoneMaskDomain(DEFAULT_MASK))
    private val _status = MutableLiveData<AuthApiStatus>()
    private val _responseAuth = MutableLiveData<AuthenticationDomain>()
    private val _isEnableButton = MutableLiveData<Boolean>(false)
    private val _isHidePassword = MutableLiveData<Boolean>(true)
    private val _phone = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    val phoneMask: LiveData<PhoneMaskDomain>
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

    /**
     * Получаем маску с сервера и подставляем сохраненные ранее данные на ее основе
     */
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

    /**
     * Аутентификация (по нажатию на "Войти")
     * */
    fun auth(login: Login){
        viewModelScope.launch {
            _status.value = AuthApiStatus.LOADING
            try{
                val response = authenticationRepository.auth(login)
                if(response.isSuccessful){
                    handleSuccessResponse(response)
                }else{
                    handleErrorResponse(response)
                }
            }catch (e: Exception) {
                _responseAuth.value = AuthenticationDomain(isSuccess = false)
                isConnectedError = true
                _status.value = AuthApiStatus.DEFAULT
            }
        }
    }

    /**
     * Обработка успешного запроса на аутентификацию
     * */
    private suspend fun handleSuccessResponse(response:Response<AuthenticationDomain>) {
        _responseAuth.value = response.body()
        _phoneMask.value?.let { phoneMask ->
            val login = Login(_phone.value!!, _password.value!!)
            authenticationRepository.savePhoneAndPassword(phoneMask.phoneMask, login)
        }
        _status.value = AuthApiStatus.DONE
    }

    /**
     * Обработка ошибки аутентификации
     * */
    private fun handleErrorResponse(response: Response<AuthenticationDomain>) {
        val errorBody = response.errorBody()?.string()
        _responseAuth.value = AuthenticationDomain(isSuccess = false)
        _status.value = AuthApiStatus.ERROR
    }

    /**
     * Сброс статуса запроса (когда переходим на экран постов)
     * */
    fun resetStatus(){
        _status.value = AuthApiStatus.DEFAULT
        isConnectedError = false
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

    /**
     * Определяем можно ли включить кнопку для входа
     * */
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