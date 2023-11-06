package com.didjeridu_dev.testtask

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.didjeridu_dev.testtask.databinding.FragmentLoginBinding
import com.didjeridu_dev.testtask.utils.MaskUtils.Companion.applyMask
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
        super.onViewCreated(view, savedInstanceState)
        var phoneMask: String = ""
        val editTextPhone = binding.etPhone

        loginViewModel.phoneMask.observe(viewLifecycleOwner) {maskContainer->
            maskContainer?.let {
                phoneMask = it.phoneMask
                editTextPhone.hint = phoneMask
            }
        }

        editTextPhone.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val formattedText = applyMask(p0.toString(), phoneMask)
                if (p0.toString() != formattedText) {
                    editTextPhone.setText(formattedText)
                    editTextPhone.setSelection(formattedText.length)
                }
            }
        })
    }
}