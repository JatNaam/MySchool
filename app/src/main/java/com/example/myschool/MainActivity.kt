package com.example.myschool

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myschool.databinding.ActivityMainBinding
import com.example.myschool.logic.model.User
import com.example.myschool.network.NetWorkStatusChangerReceiver
import com.example.myschool.network.NetWorkUtil


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private var receiver: NetWorkStatusChangerReceiver? = null

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
        binding.userNameText?.text = userName

        binding.forceOfflineBtn?.setOnClickListener {
            val intent = Intent("com.example.myschool.Force_Offline")
            sendBroadcast(intent) // 发送强制下线广播
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver = NetWorkStatusChangerReceiver() //创建广播接受器对象
        registerReceiver(receiver, intentFilter) //动态注册广播接收器
    }

    override fun onPause() {
        super.onPause()
        if (receiver != null) {
            unregisterReceiver(receiver)
            receiver = null
        }
    }

}