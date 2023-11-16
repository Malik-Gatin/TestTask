package com.didjeridu_dev.testtask.presentation

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.didjeridu_dev.testtask.R
import com.didjeridu_dev.testtask.data.network.models.Login
import com.didjeridu_dev.testtask.databinding.FragmentLoginBinding
import com.didjeridu_dev.testtask.utils.MaskUtils.Companion.applyMask
import com.didjeridu_dev.testtask.utils.MaskUtils.Companion.fromMaskToRequest
import com.didjeridu_dev.testtask.presentation.viewmodels.AuthApiStatus
import com.didjeridu_dev.testtask.presentation.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment:Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editTextPhone = binding.etPhone
        val editTextPasswd = binding.etPassword

        var phoneMask = loginViewModel.phoneMask.value?.phoneMask ?: ""
        var firstXIndex = phoneMask.indexOf('Х') - 1

        editTextPhone.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let{ loginViewModel.setPhone(it.toString()) }
            }
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    val text = it.toString()
                    val formattedText = applyMask(text, phoneMask, firstXIndex)
                    if(formattedText!=text){
                        editTextPhone.setText(formattedText)
                        editTextPhone.setSelection(formattedText.length)
                    }
                }

            }
        })

        loginViewModel.phone.observe(viewLifecycleOwner){
            it?.let {
                if(it!=editTextPhone.text.toString())
                    editTextPhone.setText(it)
            }
        }

        loginViewModel.password.observe(viewLifecycleOwner){password->
            password?.let {
                if(it!=editTextPasswd.text.toString())
                    editTextPasswd.setText(it)
            }
        }

        editTextPasswd.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let{ loginViewModel.setPassword(it.toString()) }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.apply {
            bClearPhone.setOnClickListener{
                etPhone.text?.clear()
            }

            bViewPasswd.setOnClickListener{
                loginViewModel.changeVisiblePassword()
                etPassword.setSelection(editTextPasswd.length())
            }

            bLogin.setOnClickListener{
                auth()
            }
        }

        loginViewModel.isHidePassword.observe(viewLifecycleOwner){isHide ->
            isHide.let {
                if(isHide)
                    binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                else
                    binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        loginViewModel.phoneMask.observe(viewLifecycleOwner) {maskContainer->
            maskContainer?.let {
                phoneMask = it.phoneMask
                editTextPhone.hint = phoneMask
                firstXIndex = phoneMask.indexOf('Х') - 1
            }
        }

        loginViewModel.isEnableButton.observe(viewLifecycleOwner){
            it?.let {
                enableButtonSwitcher(it)
            }
        }

        loginViewModel.status.observe(viewLifecycleOwner){authStatus->
            authStatus?.let {
                when (it) {
                    AuthApiStatus.DONE -> {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }

                    AuthApiStatus.LOADING -> {
                        loginViewModel.setEnabled(false)
                        switchShowErrorAuth(false)
                    }

                    AuthApiStatus.ERROR -> {
                        loginViewModel.setEnabled(true)
                        switchShowErrorAuth(true)
                    }
                }
            }
        }
    }

    private fun auth() {
        loginViewModel.auth(
            Login(
                loginViewModel.phone.value?.let { fromMaskToRequest(it) } ?: "",
                loginViewModel.password.value ?: ""
            )
        )
    }

    private fun enableButtonSwitcher(isEnable: Boolean){
        binding.bLogin.apply {
            isEnabled = isEnable
            this@LoginFragment.context?.let { context ->
                val backgroundButton = ContextCompat.getDrawable(
                    context,
                    R.drawable.rounded_corner
                ) as GradientDrawable
                backgroundButton.setColor(
                    ContextCompat.getColor(
                        context,
                        if(isEnable)
                            R.color.blue
                        else
                            R.color.blue_disabled
                    )
                )
                background = backgroundButton
            }
        }
    }
    private fun switchShowErrorAuth(isError: Boolean){
        binding.tvErrorPassword.apply {
            visibility = if(isError){
                View.VISIBLE
            } else{
                View.INVISIBLE
            }
        }
        binding.passwordBlock.apply {
            this@LoginFragment.context?.let { context ->
                val border = ContextCompat.getDrawable(
                    context,
                    R.drawable.rounded_corner_transparent
                ) as GradientDrawable

                border.cornerRadius = resources.getDimension(R.dimen.radius_14)
                border.setStroke(
                    resources.getDimension(R.dimen.border_width).toInt(),
                    if(isError)
                        ContextCompat.getColor(context, R.color.red_light)
                    else
                        ContextCompat.getColor(context, R.color.gray_extralight)
                )
                background = border
            }
        }
    }
}