package com.example.myschool.ui.function

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myschool.databinding.FragmentStudyBinding

class StudyFragment:Fragment() {
    private var _binding: FragmentStudyBinding? = null

    private val binding get() = _binding!!

    private val stuTransactionList: MutableList<String> = mutableListOf(
            "· 办事大厅",
            "· 教师邮箱@",
            "· 本科生教务",
            "· 研究生教务",
            "· Blackboard",
            "· OA系统",
            "· 科学技术",
            "· 社会科学",
            "· 人力资源",
            "· 实验设备",
            "· 设备共享",
            "· 财务服务",
            "· 招标采购")

    private val teaTransactionList: MutableList<String> = mutableListOf(
            "· UOOC课程",
            "· 学生邮箱@",
            "· 本科生选课",
            "· 研究生选课",
            "· 学生工作",
            "· 事务中心",
            "· 公寓管理",
            "· 心理咨询",
            "· 缴学杂费",
            "· 考研考博",
            "· 就业指导",
            "· 学生活动",
            "· 用电查询")

    private lateinit var leftTransactionAdapter: LeftTransactionAdapter
    private lateinit var rightTransactionAdapter: RightTransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudyBinding.inflate(inflater, container, false)

        val layoutManager1 = LinearLayoutManager(activity)
        binding.stuRecycleList.layoutManager = layoutManager1
        leftTransactionAdapter =
            LeftTransactionAdapter(
                stuTransactionList
            )
        binding.stuRecycleList.adapter=leftTransactionAdapter

        val layoutManager2 = LinearLayoutManager(activity)
        binding.teaRecycleList.layoutManager = layoutManager2
        rightTransactionAdapter =
            RightTransactionAdapter(
                teaTransactionList
            )
        binding.teaRecycleList.adapter=rightTransactionAdapter

        return binding.root
    }

}
