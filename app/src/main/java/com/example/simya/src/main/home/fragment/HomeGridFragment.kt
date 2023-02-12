package com.example.simya.src.main.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simya.R
import com.example.simya.config.BaseFragment
import com.example.simya.util.Constants.HOUSE_ID
import com.example.simya.util.Constants.OK
import com.example.simya.src.main.story.StoryIntroActivity
import com.example.simya.databinding.FragmentHomeMainGridBinding
import com.example.simya.src.main.home.adapter.HomeGVAdapter
import com.example.simya.src.main.home.model.AllStoryInterface
import com.example.simya.src.main.home.model.AllStoryService
import com.example.simya.util.data.UserData
import com.example.simya.src.model.story.load.LoadAllStoryResponse
import com.example.simya.src.model.story.load.LoadAllStoryResult
import com.example.simya.util.Constants.ERROR_STRING_NULL_STORY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeGridFragment : BaseFragment<FragmentHomeMainGridBinding>(
    FragmentHomeMainGridBinding::bind,
    R.layout.fragment_home_main_grid
), AllStoryInterface {
    private var dataList: ArrayList<LoadAllStoryResult> = arrayListOf()
    private lateinit var dataGVAdapter: HomeGVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        AllStoryService(this).tryGetAllStory()
    }

    private fun clickStory() {
        dataGVAdapter.setOnItemClickListener(object : HomeGVAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: LoadAllStoryResult, position: Int) {
                //뭐보낼까
                val intent = Intent(this@HomeGridFragment.context, StoryIntroActivity::class.java)
                intent.putExtra(HOUSE_ID, data.houseId)
                startActivity(intent)

            }

            override fun onLongClick(v: View, data: LoadAllStoryResult, position: Int) {
            }

        })
    }

    override fun onGetAllStorySuccess(response: LoadAllStoryResponse) {
        if(response.message.equals(ERROR_STRING_NULL_STORY)){
            Log.d("onGetAllStorySuccess",response.message!!)
        }else{
            requireActivity().runOnUiThread {
                val resource = response.result
                dataList.add(resource as LoadAllStoryResult)
                val gridLayoutManager = GridLayoutManager(activity, 2)
                dataGVAdapter = HomeGVAdapter(dataList)
                binding.gvHomeMainGrid.adapter = dataGVAdapter
                binding.gvHomeMainGrid.layoutManager = gridLayoutManager
                clickStory()
            }
        }
    }

    override fun onGetAllStoryFailure(response: LoadAllStoryResponse) {
        Log.d("@@@@@ CHECK @@@@@@", "이야기집 가져오기")
    }
}