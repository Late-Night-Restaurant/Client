package com.example.simya.src.main.myPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.simya.config.BaseActivity
import com.example.simya.databinding.ActivityMyPageReviewBinding
import com.example.simya.util.dialog.SortDialog
import com.example.simya.src.main.myPage.fragment.MyPageReviewFragment

class MyPageReviewActivity : BaseActivity<ActivityMyPageReviewBinding>(ActivityMyPageReviewBinding::inflate)
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }
    private fun init() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fmMyPageReview.id, MyPageReviewFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        // 정렬 타입 바꾸기
        binding.ibMyPageReviewType.setOnClickListener {
            val dialog = SortDialog(this as AppCompatActivity)
            dialog!!.showDia()
        }
    }
}