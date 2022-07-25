package com.example.myschool.ui.login

import android.os.Bundle
import com.example.myschool.BaseActivity
import com.example.myschool.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity(){
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}