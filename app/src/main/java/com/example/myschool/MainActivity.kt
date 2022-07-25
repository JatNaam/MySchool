package com.example.myschool

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myschool.databinding.ActivityMainBinding
import com.example.myschool.logic.model.User

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = intent.getSerializableExtra("userData") as User
        binding.loggedUserHeadPortrait?.let {
            Glide.with(this).load(user.userHeadPortraitPath)
                .apply(MySchoolApplication.requestOptions)
                .into(it)
        }
        val userName = user.userName
        val account = user.userAccount
        binding.userNameText?.text = userName
        binding.forceOfflineBtn?.setOnClickListener {
            val intent = Intent("com.example.myschool.FORCE_OFFLINE")
            intent.putExtra("currentAccount", account)
            sendBroadcast(intent) // 发送强制下线广播
        }
    }
}