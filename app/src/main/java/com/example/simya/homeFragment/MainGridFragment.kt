package com.example.simya.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simya.activity.testData.TestData
import com.example.simya.databinding.FragmentHomeMainGridBinding
import com.example.simya.homeAdapter.MainGVAdapter
import com.example.simya.homeAdapter.MainRVAdapter

class MainGridFragment: Fragment() {
    private lateinit var binding: FragmentHomeMainGridBinding
    private lateinit var dataList: ArrayList<TestData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMainGridBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init(){
        dataList = arrayListOf()
        dataList.apply{
            add(TestData(3,"코딩","안드로이드","코딩은 재밌어"))
            add(TestData(1,"코딩","서버","코딩은 재밌어"))
            add(TestData(2,"코딩","안드로이드","코딩은 힘들어"))
            add(TestData(4,"코딩","서버","코딩은 힘들어"))
        }
        val dataGVAdapter = MainGVAdapter(dataList)
        val gridLayoutManager = GridLayoutManager(this.context,2)
        binding.gvHomeMainGrid.adapter = dataGVAdapter
        binding.gvHomeMainGrid.layoutManager = gridLayoutManager
    }
}