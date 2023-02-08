package com.example.simya.src.fragment.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simya.R
import com.example.simya.src.activity.myPage.MyPageLikeActivity
import com.example.simya.src.activity.myPage.MyPageReviewActivity
import com.example.simya.src.activity.myPage.ProfileEditActivity
import com.example.simya.databinding.FragmentHomeMyPageBinding
import com.example.simya.src.adpter.myPage.MultiProfileAdapter
import com.example.simya.src.testData.TestDataMultiProfile

class MyPageProfileFragment: Fragment() {
    private lateinit var binding: FragmentHomeMyPageBinding
    private lateinit var dataList: ArrayList<TestDataMultiProfile>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMyPageBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        // Fragment에서 Activity로 넘어가깅!!
        // Fragment는 this 사용 불가이므로, this 대신 activity를 씀
        binding.btnMyPageProfile.setOnClickListener {
            val intent = Intent(activity, ProfileEditActivity::class.java)
            startActivity(intent)
        }

        // 찜한 이야기 집으로
        binding.ibMyPageMenu1.setOnClickListener {
            val intent = Intent(activity, MyPageLikeActivity::class.java)
            startActivity(intent)
        }

        // 내가 쓴 리뷰로
        binding.ibMyPageMenu2.setOnClickListener {
            val intent = Intent(activity, MyPageReviewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init() {
        dataList = arrayListOf()
        dataList.apply {
            add(dataList.size,TestDataMultiProfile(R.drawable.ic_plus_small, "추가하기","추가하기"))
        }
        val dataRVAdapter = MultiProfileAdapter(this,dataList)
        binding.rvMyPageMultiProfile.adapter = dataRVAdapter
        binding.rvMyPageMultiProfile.layoutManager =
            LinearLayoutManager(this.context,
                RecyclerView.HORIZONTAL,
                false)
    }

}