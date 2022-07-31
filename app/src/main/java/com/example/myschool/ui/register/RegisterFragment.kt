package com.example.myschool.ui.register

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myschool.MySchoolApplication
import com.example.myschool.R
import com.example.myschool.databinding.FragmentRegisterBinding
import com.example.myschool.logic.AppDatabase
import com.example.myschool.logic.model.User
import com.example.myschool.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private val takePhoto = 1
    private val fromAlbum = 2
    private lateinit var headPortraitUri: Uri
    private lateinit var headPortrait: File
    private lateinit var headPortraitPath: String
    private var isGetHeadPortraitPath:Boolean=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val activity = activity

        val userDao = AppDatabase.getDatabase(MySchoolApplication.context).getUserDao()

        binding.registerAccountEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.length !in 6..10) {
                binding.registerAccountEdit.error = "输入不可小于6位且不可超出10位"
            }
        }

        binding.registerPasswordEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.length !in 8..15) {
                binding.registerPasswordEdit.error = "输入不可小于8位且不可超出15位"
            }
        }

        binding.registerNameEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isEmpty()) {
                binding.registerNameEdit.error = "用户名不可为空"
            }
        }

        binding.userHeadPortraitSetter.setOnClickListener {
            setHeadPortrait()
        }

        binding.confirmBtn.setOnClickListener {
            val account:String = binding.registerAccountEdit.text.toString()
            val password: String = binding.registerPasswordEdit.text.toString()
            val userName: String = binding.registerNameEdit.text.toString()
            if (account.length in 6..10 && password.length in 8..15 && userName.isNotEmpty()&&isGetHeadPortraitPath) {
                thread {
                    val user: User? = userDao.loadUser(account)
                    if (user == null) {
                        val newUser = User(
                            account, password, userName,
                            userStatus = true,
                            isLogged = false,
                            headPortraitPath
                        )
                        newUser.id = userDao.insertUser(newUser)
                        val intent = Intent(context, LoginActivity::class.java).apply {
                            putExtra("registerAccount", account)
                            putExtra("STATUS", "REGISTER")
                        }
                        startActivity(intent)
                        activity?.finish() //跳转后，销毁返回键的日志
                    } else
                        activity?.runOnUiThread {
                            Toast.makeText(
                                MySchoolApplication.context, "This account is already existed!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            } else
                Toast.makeText(
                    MySchoolApplication.context, "The registration format is invalid!",
                    Toast.LENGTH_SHORT
                ).show()
        }

        return binding.root
    }

    /*更换头像*/
    @SuppressLint("InflateParams")
    private fun setHeadPortrait() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_bottom_register, null)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.setBackgroundColor(
                Color.TRANSPARENT
            )
        val tvTakePictures: TextView = dialogView.findViewById(R.id.tv_take_pictures)
        val tvOpenAlbum: TextView = dialogView.findViewById(R.id.tv_open_album)
        val tvCancel: TextView = dialogView.findViewById(R.id.tv_cancel)

        //拍照
        tvTakePictures.setOnClickListener {
            takePhoto()
            bottomSheetDialog.cancel()
        }
        //打开相册
        tvOpenAlbum.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //动态申请读写SD卡的权限
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                openAlbum()
            }
            bottomSheetDialog.cancel()
        }
        //取消
        tvCancel.setOnClickListener { bottomSheetDialog.cancel() }
        //底部弹窗显示
        bottomSheetDialog.show()
    }

    /*拍照*/
    @SuppressLint("SimpleDateFormat")
    private fun takePhoto() {
        // 创建File对象，用于存储拍照后的图片
        val timeStampFormat = SimpleDateFormat(
            "yyyy_MM_dd_HH_mm_ss"
        )
        val filename = timeStampFormat.format(Date())
        headPortrait = File(requireContext().externalCacheDir, filename + "headPortraits.jpg")
        if (headPortrait.exists()) {
            headPortrait.delete()
        }
        headPortrait.createNewFile()
        headPortraitUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                requireContext(),
                "com.example.myschool.fileprovider",
                headPortrait
            )
        } else {
            Uri.fromFile(headPortrait)
        }
        // 启动相机程序
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, headPortraitUri)
        headPortraitPath = headPortrait.absolutePath
        startActivityForResult(intent, takePhoto)
    }

    /*打开相册*/
    private fun openAlbum() {
        // 打开文件选择器
        val intent = Intent("android.intent.action.GET_CONTENT")
        // 指定只显示照片
        intent.type = "image/*"
        startActivityForResult(intent, fromAlbum)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    displayImage(headPortraitPath)
                }
            }
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    getHeadPortraitPath(data)
                }
            }
        }
    }

    private fun getHeadPortraitPath(data: Intent) {
        var imagePath: String? = null
        val uri = data.data
        Log.d("TAG", "handleImageOnKitKat: uri is $uri")
        if (DocumentsContract.isDocumentUri(requireContext(), uri)) {
            // 如果是document类型的Uri，则通过document id处理
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri!!.authority) {
                val id = docId.split(":").toTypedArray()[1] // 解析出数字格式的id
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(docId)
                )
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.path
        }
        headPortraitPath = imagePath ?: null.toString()
        displayImage(imagePath)
    }

    @SuppressLint("Range")
    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        // 通过Uri和selection来获取真实的图片路径
        val cursor: Cursor? =
            requireContext().contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    /*动态申请权限时会回调到该方法
    * 该方法要在Fragment的Activity也重写之后，
    * */
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum()
            } else {
                Toast.makeText(
                    MySchoolApplication.context, "You denied the permission!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /*通过图片路径显示图片*/
    private fun displayImage(imagePath: String?) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
            Glide.with(this).load(imagePath).apply(MySchoolApplication.requestOptions)
                .into(binding.userHeadPortraitSetter)
            isGetHeadPortraitPath=true
        }
        else
            Toast.makeText(
                MySchoolApplication.context, "Failed to get image!",
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}