package com.example.myschool.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myschool.databinding.FragmentViewpagerBinding
import java.util.*


private const val ARG_SECTION_NUMBER = "section_number"

class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewpagerBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewPagerViewModel: ViewPagerViewModel

    private val noticeList: MutableList<String> = mutableListOf("0")

    private lateinit var noticeAdapter: NoticeAdapter

    private var flag = 0

    companion object {
        fun newInstance(index: Int): ViewPagerFragment {
            val fragment = ViewPagerFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPagerViewModel = ViewModelProvider(this).get(ViewPagerViewModel::class.java)
        val index = requireArguments().getInt(ARG_SECTION_NUMBER)
        viewPagerViewModel.setIndex(index)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewpagerBinding.inflate(inflater, container, false)

        val layoutManagerNotice = LinearLayoutManager(activity)
        binding.noticeRecycleList.layoutManager = layoutManagerNotice
        initNotice()
        noticeAdapter = NoticeAdapter(noticeList)
        binding.noticeRecycleList.adapter = noticeAdapter

        /*对ViewPager的页码进行观察*/
        viewPagerViewModel.index.observe(
            viewLifecycleOwner
        ) { index ->
            /*当观察的值发生变化时会回调到这里，
            * （这里观察的值时ViewPage的页码）
            * 所以在这里更新UI*/
            refresh(index)
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(flag: Int) {
        if (this.flag == flag) return else this.flag = flag
        initNotice()
        noticeAdapter.notifyDataSetChanged()//通知数据发生变化
    }

    private val notices: Array<String> = arrayOf(
        "·[行政]关于参加教育系统《信访工作条例》培训视频会…",
        "·[行政]关于做好假期实验室安全管理工作的通知",
        "·[讲座]学术讲座（八）：未来食品开发创新思路",
        "·[科研]2022年度学校体育卫生艺术国防教育专项任…",
        "·[教务]深圳大学2022届成人本科毕业生学士学位授…",
        "·[教务]2021-2022学年普高本科生专业调整结果",
        "·[生活]关于丽湖校区7月27日核酸检测通知",
        "·[生活]关于粤海校区7月27日开展全员核酸检测的通知",
        "·[生活]2022年深圳大学医学部医学人文周“身边的…",
        "·[教务]2022年深圳大学微纳光电子学研究院暑期学…",
        "·[科研]我校获1项国家统计局2022年度全国统计科…"
    )

    private fun initNotice() {
        noticeList.clear()
        val random = Random()
        for (i in 0..40) {
            val index = random.nextInt(notices.size)
            noticeList.add(i, notices[index])
        }
    }
}


