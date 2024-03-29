package com.example.myschool.network

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myschool.ActivityCollector
import com.example.myschool.MySchoolApplication
import com.example.myschool.ui.login.LoginActivity

//继承广播接受器类
class NetWorkStatusChangerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (!NetWorkUtil.isNetworkConnected(MySchoolApplication.context)) {
            AlertDialog.Builder(context).apply {
                setTitle("Warning")
                setMessage("Network connection down. Please try to login again.")
                setCancelable(false)
                setPositiveButton("OK") { _, _ ->
                    ActivityCollector.finishAll() // 销毁所有活动
                    val intent1 = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent1) // 重新启动LoginActivity
                }
                show()
            }
        }
    }
}