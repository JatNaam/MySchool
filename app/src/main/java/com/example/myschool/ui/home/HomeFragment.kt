package com.example.myschool.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myschool.MainActivity
import com.example.myschool.MySchoolApplication
import com.example.myschool.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.menuBtn.setOnClickListener {
            val activity=activity as MainActivity
            activity.openDrawerLayout()
        }

        /*按钮的文本显示不全，要设置边距*/
        binding.officialDocumentPassBtn.setPadding(1,1,1,1)
        binding.officialDocumentPassBtn.setOnClickListener{
            Toast.makeText(
                MySchoolApplication.context, "你点击了进入公文通的按钮，功能尚未开发！",
                Toast.LENGTH_SHORT
            ).show()
        }
        initViewPager()
        return binding.root
    }

    private val tabs = arrayOf("重要通知" , "深大新闻", "学术讲座")

    private fun initViewPager() {
        //viewPager的适配器
        binding.viewPager.adapter = ViewPagerStateAdapter(requireActivity())
        //绑定tabLayout和viewPager
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}

