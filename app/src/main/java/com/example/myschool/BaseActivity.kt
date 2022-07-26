package com.example.myschool

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myschool.ActivityCollector.addActivity
import com.example.myschool.ActivityCollector.finishAll
import com.example.myschool.ActivityCollector.removeActivity
import com.example.myschool.ui.login.LoginActivity

open class BaseActivity : AppCompatActivity() {
    private var receiver: ForceOfflineReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity(this)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.myschool.Force_Offline")
        receiver = ForceOfflineReceiver() //创建广播接受器对象
        registerReceiver(receiver, intentFilter) //动态注册广播接收器
    }

    override fun onPause() {
        super.onPause()
        if (receiver != null) {
            unregisterReceiver(receiver)
            receiver = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeActivity(this)
    }

    inner class ForceOfflineReceiver : BroadcastReceiver() {
        //继承广播接受器类
        override fun onReceive(context: Context, intent: Intent) { //这个intent接受广播传输的数据
            AlertDialog.Builder(context).apply {
                setTitle("Warning")
                setMessage("You are forced to offline. Please try to login again.")
                setCancelable(false)
                setPositiveButton("OK") { _, _ ->
                    finishAll() // 销毁所有活动
                    val intent1 = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent1) // 重新启动LoginActivity
                }
                show()
            }
        }
    }
}