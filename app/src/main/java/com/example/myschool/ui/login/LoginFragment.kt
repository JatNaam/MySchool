package com.example.myschool.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myschool.MainActivity
import com.example.myschool.MySchoolApplication
import com.example.myschool.R
import com.example.myschool.databinding.FragmentLoginBinding
import com.example.myschool.logic.AppDatabase
import com.example.myschool.logic.dao.UserDao
import com.example.myschool.logic.model.User
import com.example.myschool.network.NetWorkUtil
import com.example.myschool.ui.register.RegisterActivity
import kotlin.concurrent.thread

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val activity = activity

        val sp: SharedPreferences =
            MySchoolApplication.context.getSharedPreferences("LoggedAccountBefore", 0)

        userDao = AppDatabase.getDatabase(MySchoolApplication.context).getUserDao()

        binding.loginAccountEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                thread {
                    val user: User? = userDao.loadUser(content)
                    if (user != null) {
                        activity?.runOnUiThread {
                            Glide.with(this).load(user.userHeadPortraitPath)
                                .apply(MySchoolApplication.requestOptions)
                                .into(binding.userHeadPortrait)
                            binding.rememberCheck.isChecked = user.userStatus
                            if (user.userStatus) {
                                binding.loginPasswordEdit.setText(user.userPassword)
                            } else binding.loginPasswordEdit.text = null
                        }
                    }
                }
            }
            if (content.length !in 6..10) {
                binding.loginAccountEdit.error = "输入不可小于6位且不可超出10位"
            }
        }
        binding.loginPasswordEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.length !in 8..15) {
                binding.loginPasswordEdit.error = "输入不可小于8位且不可超出15位"
            }
        }

        /*判断是在什么情况打开登录界面的*/
        var intent = activity?.intent
        if (intent != null) {
            when (intent.getStringExtra("STATUS")) {
                null -> {
                    /*重新打开程序、填上上一个登录的账号*/
                    val loggedAccountBefore: String? = sp.getString("loggedAccountBefore", null)
                    binding.loginAccountEdit.setText(loggedAccountBefore)
                }
                "REGISTER" -> {
                    //填上刚刚注册完成的账号
                    val registerAccount = intent.getStringExtra("registerAccount")
                    binding.loginAccountEdit.setText(registerAccount)
                }
            }
        }

        binding.loginBtn.setOnClickListener {
            val account: String = binding.loginAccountEdit.text.toString()
            val password: String = binding.loginPasswordEdit.text.toString()
            if (NetWorkUtil.isNetworkConnected(MySchoolApplication.context)){
                if (account.length in 6..10 && password.length in 8..15) {
                    thread {
                        val user: User? = userDao.loadUser(account)
                        if (user != null) {
                            if (user.userPassword == password) {
                                user.userStatus = binding.rememberCheck.isChecked
                                user.isLogged = true
                                userDao.updateUser(user)
                                /*利用intent传递序列化之后的对象数据*/
                                intent = Intent(context, MainActivity::class.java).apply {
                                    putExtra("userData", user)
                                }
                                /*保存登录的账号，用于重新打开程序时填写信息*/
                                /*重新更新users的数据*/
                                @SuppressLint("CommitPrefEdits") val editor = sp.edit()
                                editor.putString("loggedAccountBefore", account)
                                editor.apply()

                                startActivity(intent)
                                activity?.finish() //跳转后，销毁返回键的日志
                            } else {
                                activity?.runOnUiThread {
                                    Toast.makeText(
                                        MySchoolApplication.context, "Password error!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.loginPasswordEdit.text = null
                                }
                            }
                        } else {
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    MySchoolApplication.context, "This account is not exist!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.loginAccountEdit.text = null
                                binding.loginPasswordEdit.text = null
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        MySchoolApplication.context, "Input error!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    MySchoolApplication.context, "Please check your network status!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.registerBtn.setOnClickListener {
            if (NetWorkUtil.isNetworkConnected(MySchoolApplication.context)){
                intent = Intent(context, RegisterActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(
                    MySchoolApplication.context, "Please check your network status!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.showListBtn.setOnClickListener { showPopupWindow() }

        return binding.root
    }

    private fun showPopupWindow() {
        //设置contentView
        @SuppressLint("InflateParams") val contentView: View =
            layoutInflater.inflate(R.layout.popupwindow_accounts, null)
        val mPopWindow = PopupWindow(
            contentView,
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
        )
        mPopWindow.contentView = contentView

        //实现PopupWindow+ListView切换账户
        val listView = contentView.findViewById<View>(R.id.accountList) as ListView
        thread {
            val loggedUsers: List<User> = userDao.loadLoggedUsers(true)
            if (loggedUsers.isNotEmpty()) {
                val adapter = AccountAdapter(activity, R.layout.account_item, loggedUsers)
                listView.adapter = adapter
                listView.onItemClickListener =
                    OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                        activity?.runOnUiThread {
                            binding.loginAccountEdit.setText(loggedUsers[position].userAccount)
                            /*关闭PopupWindow*/
                            mPopWindow.dismiss()
                        }
                    }
            }
        }
        //显示PopupWindow
        mPopWindow.showAsDropDown(binding.showListBtn)
    }

    inner class AccountAdapter(
        context: Context?, private val resourceId: Int,
        objects: List<User?>?
    ) :
        ArrayAdapter<User?>(context!!, resourceId, objects!!) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val accountItem = getItem(position) // 获取当前项的实例
            val view: View
            val viewHolder: ViewHolder
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(resourceId, parent, false)
                viewHolder = ViewHolder()
                viewHolder.account = view.findViewById<View>(R.id.account) as TextView
                view.tag = viewHolder // 将ViewHolder存储在View中
            } else {
                view = convertView
                viewHolder = view.tag as ViewHolder // 重新获取ViewHolder
            }
            if (accountItem != null) {
                viewHolder.account.text = accountItem.userAccount
            }
            return view
        }

        inner class ViewHolder {
            lateinit var account: TextView
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}