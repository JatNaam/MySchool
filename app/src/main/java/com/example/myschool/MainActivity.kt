package com.example.myschool

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.myschool.databinding.ActivityMainBinding
import com.example.myschool.logic.model.User
import com.example.myschool.network.NetWorkStatusChangerReceiver
import com.example.myschool.ui.function.LifeActivity
import com.example.myschool.ui.function.StudyActivity
import com.google.android.material.imageview.ShapeableImageView

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private var receiver: NetWorkStatusChangerReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent?.getSerializableExtra("userData") as User

        val loggedUserHeadPortrait = binding.navView.getHeaderView(0)
            .findViewById<ShapeableImageView>(R.id.loggedUserHeadPortrait)
        loggedUserHeadPortrait.let {
            Glide.with(this).load(user.userHeadPortraitPath)
                .apply(MySchoolApplication.requestOptions)
                .into(it)
        }
        val userNameText =
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.userNameText)
        userNameText.text = user.userName
        val userAccountText =
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.userAccountText)
        userAccountText.text = user.userAccount

        val logOutBtn =
            binding.navView.getHeaderView(0).findViewById<Button>(R.id.logOutBtn)
        logOutBtn.setOnClickListener{
            intent = Intent("com.example.myschool.Force_Offline")
            sendBroadcast(intent) // 发送强制下线广播
        }

        if (binding.tabletLayout==null){
            binding.navView.setNavigationItemSelectedListener { //设置滑动菜单中item的点击事件，这里调用了将滑动菜单关闭的方法
                when (it.itemId) {
                    R.id.navStudy ->{
                        val intent = Intent(this, StudyActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.navLife ->{
                        val intent = Intent(this, LifeActivity::class.java)
                        startActivity(intent)
                    }
                }
                binding.drawerLayout.closeDrawers()
                true
            }
        }
    }

    fun openDrawerLayout(){
        binding.drawerLayout.open()
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