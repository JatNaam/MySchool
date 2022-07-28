package com.example.myschool.ui.function

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import com.example.myschool.BaseActivity
import com.example.myschool.databinding.ActivityStudyBinding
import com.example.myschool.network.NetWorkStatusChangerReceiver

class StudyActivity:BaseActivity() {
    private lateinit var binding: ActivityStudyBinding
    private var receiver: NetWorkStatusChangerReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)
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