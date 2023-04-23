package com.example.myschool.ui.function

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myschool.databinding.FragmentLifeBinding

class LifeFragment : Fragment() {
    private var _binding: FragmentLifeBinding? = null

    private val binding get() = _binding!!

    private val realTransactionList: MutableList<String> = mutableListOf(
        "· 深大义工",
        "· 学科竞赛",
        "· 创新创业",
        "· 工会活动",
        "· 深大校报",
        "· 网上电视",
        "· 广播电台",
        "· 视频校园",
        "· 体育场馆",
        "· 后勤服务",
        "· 国际交流",
        "· 总 医 院",
        "· 华南医院"
    )

    private val netTransactionList: MutableList<String> = mutableListOf(
        "· 图 书 馆",
        "· 校园网络",
        "· 自助服务",
        "· 校务信箱",
        "· 短信平台",
        "· 会议室申请",
        "· 校史馆预约",
        "· 校 园 卡",
        "· 故障报修",
        "· 失物招领",
        "· 基 金 会",
        "· 深大纪念品",
        "· 中行网银",
    )

    private lateinit var leftTransactionAdapter: LeftTransactionAdapter
    private lateinit var rightTransactionAdapter: RightTransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifeBinding.inflate(inflater, container, false)

        val layoutManager1 = LinearLayoutManager(activity)
        binding.realRecycleList.layoutManager = layoutManager1
        leftTransactionAdapter =
            LeftTransactionAdapter(
                realTransactionList
            )
        binding.realRecycleList.adapter = leftTransactionAdapter

        val layoutManager2 = LinearLayoutManager(activity)
        binding.netRecycleList.layoutManager = layoutManager2
        rightTransactionAdapter =
            RightTransactionAdapter(
                netTransactionList
            )
        binding.netRecycleList.adapter = rightTransactionAdapter

        return binding.root
    }

}
