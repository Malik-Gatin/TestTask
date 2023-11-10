package com.didjeridu_dev.testtask

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.didjeridu_dev.testtask.data.network.models.Login
import com.didjeridu_dev.testtask.databinding.FragmentLoginBinding
import com.didjeridu_dev.testtask.utils.MaskUtils.Companion.applyMask
import com.didjeridu_dev.testtask.utils.MaskUtils.Companion.fromMaskToRequest
import com.didjeridu_dev.testtask.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment:Fragment() {

    private val loginViewModel:LoginViewModel by viewModels()
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
        val editTextPhone = binding.etPhone
        val editTextPasswd = binding.etPassword

        super.onViewCreated(view, savedInstanceState)
        var phoneMask = ""
        var firstXIndex = -1


        loginViewModel.phoneMask.observe(viewLifecycleOwner) {maskContainer->
            maskContainer?.let {
                phoneMask = it.phoneMask
                editTextPhone.hint = phoneMask
                firstXIndex = phoneMask.indexOf('Х') - 1
            }
        }

        editTextPhone.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    val text = p0.toString()
                    val formattedText = applyMask(text, phoneMask, firstXIndex)

                    if (text != formattedText) {
                        editTextPhone.setText(formattedText)
                        editTextPhone.setSelection(formattedText.length)
                    }
                }
            }
        })

        binding.bLogin.setOnClickListener{
            val phone = fromMaskToRequest(editTextPhone.text.toString())
            val password = editTextPasswd.text.toString()
            auth(
                Login(phone = phone, password = password)
            )
        }
    }

    private fun auth(loginData: Login){
        loginViewModel.auth(loginData)
        loginViewModel.responseAuth.observe(viewLifecycleOwner) {authContainer->
            authContainer?.let {
                Toast.makeText(this@LoginFragment.context, it.isSuccess.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}