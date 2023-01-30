package com.example.simya.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simya.Constants
import com.example.simya.R
import com.example.simya.adpter.chatAdapter.ChatDrawerRVAdapter
import com.example.simya.adpter.chatAdapter.ChatRVAdapter
import com.example.simya.databinding.ActivityDrawerChatBinding
import com.example.simya.testData.TestChatData
import com.example.simya.testData.TestChatDrawerProfileData
import com.example.simya.testData.TestUserData
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.gmail.bishoybasily.stomp.lib.constants.Headers
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.internal.http2.Header
import org.json.JSONObject


class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityDrawerChatBinding
    private lateinit var dataList: ArrayList<TestChatData>
    private lateinit var profileList: ArrayList<TestChatDrawerProfileData>
    lateinit var sendUser: TestUserData
    lateinit var receiveUser1: TestUserData
    lateinit var receiveUser2: TestUserData
    lateinit var receiveUser3: TestUserData
    lateinit var receiveUser4: TestUserData

    //Stomp Test
    val url = "ws://10.0.2.2:8080/ws-stomp"
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val intervalMillis = 1000L
    private lateinit var jSONObject: JSONObject

    //    private val client = OkHttpClient()
    val client = OkHttpClient.Builder()
        .addInterceptor {
            it.proceed(
                it.request().newBuilder().header(
                    "Access-Token",
                    "Access eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhODExODE5OUBnbWFpbC5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjc1MTU0MTgzfQ.Ioq_fdA_OIHsiYu71b8OIoJf7j8u1t7So-HZ_ns_9IzzSxYdKxZhtNglRmXDiW3uucbQUC5NMg1GKkjppC3oVQ"
                )
                    .header(
                        "Refresh-Token",
                        "Refresh eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NzU2MjU5MDB9.HH-OuX4wmFaJrWdHiU9S8zTrEia3yrmeEFXkdLyt9yGdS32x5uclTrY4iSddcFZL6CfqomE4AiKgkieHNR_nLQ"
                    ).build()
            )
        }
        .build()

    private val stomp = StompClient(client, intervalMillis)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        유저 체크
//        userCheck()
//        Log.d("Stomp Client Conect",stompClient.connect().toString())
//        stompClient.connect()
//        stompClient.topic("/sub/simya/chat/room/32bc75f2-903e-4a26-bde7-fa7e266e750a").subscribe { topicMessage ->
//            Log.d(
//                TAG,
//                topicMessage.getPayload()
//            )
//        }
//        Log.d("Stomp Client", stompClient.isConnected.toString())
        testUserCheck(Constants.CHAT_GUEST_CODE)
        init()
    }

    private fun init() {
        stomp.url = "ws://10.0.2.2:8080/simya/ws-stomp/websocket"
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    Log.d("CONNECT", "OPENED")
                    topic = stomp.join("/sub/simya/chat/room/53e9a49e-99be-4efd-857f-90655f7689fb")
                        .subscribe {
                            Header(
                                "Access-Token",
                                "Access eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhODExODE5OUBnbWFpbC5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjc1MTU0MTgzfQ.Ioq_fdA_OIHsiYu71b8OIoJf7j8u1t7So-HZ_ns_9IzzSxYdKxZhtNglRmXDiW3uucbQUC5NMg1GKkjppC3oVQ"
                            )
                            Header(
                                "Refresh-Token",
                                "Refresh eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NzU2Njk5OTR9.L2c57pF5h-wiRkUYHi9orVHhYltgOa7w-4m5a_jirckvsU6tNku-jbQtan1mVokgXqHD61JhYmQqyS7FvOX-yw"
                            )
                        }

                }
                Event.Type.CLOSED -> {
                    Log.d("CONNECT", "CLOSED")
                }
                Event.Type.ERROR -> {
                    Log.d("CONNECT", "ERROR")
                    Log.d("Client", client.toString())
                }
                else -> {}
            }
        }

        val drawerLayout = binding.dlChat
        binding.includedChat.includedDefault.tvDefaultChatTitle.text = "테스트용 이야기방"
        binding.includedChat.includedDefault.ibDefaultChatDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        // 테스트 유저 초기화
        testUserInit()

        //표시할 채팅 리스트
        dataList = arrayListOf()
        setTestData()
        val dataRVAdapter = ChatRVAdapter(this, dataList)
        binding.includedChat.rvChatList.adapter = dataRVAdapter
        binding.includedChat.rvChatList.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
            moveLastItem(this)
        }

        // test용 메시지 보내기
        binding.includedChat.ibChatSend.setOnClickListener {
            if (binding.includedChat.etChatInput.text.isNotEmpty()) {
                Log.d("send message", binding.includedChat.etChatInput.text.toString())
                sendMessage(
                    sendUser,
                    binding.includedChat.etChatInput.text.toString(),
                    dataRVAdapter
                )
                jSONObject = JSONObject()
                jSONObject.put("type", "TALK")
                jSONObject.put("roomId", "53e9a49e-99be-4efd-857f-90655f7689fb")
                jSONObject.put("sender", "choi")
                jSONObject.put("token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhODExODE5OUBnbWFpbC5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjc1MTU0MTgzfQ.Ioq_fdA_OIHsiYu71b8OIoJf7j8u1t7So-HZ_ns_9IzzSxYdKxZhtNglRmXDiW3uucbQUC5NMg1GKkjppC3oVQ")
                jSONObject.put("message", "성공입니다.")
                jSONObject.put("userCount", 1)
                Log.d("Check JSON OBJECT",jSONObject.toString())
//                var messageMapping = HashMap<String,String>()
//                messageMapping["token"] = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhODExODE5OUBnbWFpbC5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjc1MTA3NTAwfQ.kqePlFHP5Wj1tAmBBYtDnK0vdUGrTn5k-Xi38Zbyb8CT9-TmliL5-EaiZkP-fnmiBTnCuGNtLnG1FBf_j-p5Gg"
                stomp.send(
                    "/pub/simya/chat/androidMessage",
                    jSONObject.toString()
                ).subscribe {
                    if(it) {
                    }
                }
            }

            testDrawerUserListed()
        }

    }


    private fun moveLastItem(layoutManager: LinearLayoutManager) {
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable { layoutManager.scrollToPositionWithOffset(dataList.size - 1, 0) }, 300
        )
    }

    private fun testUserCheck(code: Int) {
        if (code == Constants.CHAT_MASTER_CODE) {
            // 주인장 캐스팅
        } else if (
            code == Constants.CHAT_GUEST_CODE) {
            setGuestType()
        }
    }

    private fun userCheck(user: TestUserData) {
        if (user.type == Constants.CHAT_MASTER_CODE) {
            // 주인장 캐스팅
        } else if (
            user.type == Constants.CHAT_GUEST_CODE) {
            setGuestType()
        }
    }

    private fun setMasterType() {
        // 딱히없음
    }

    private fun setGuestType() {
        binding.btnChatPause.isInvisible = true
        binding.ibChatCloseOrLike.setImageResource(R.drawable.ic_heart)
    }

    private fun sendMessage(user: TestUserData, content: String, adapter: ChatRVAdapter) {
        dataList.add(TestChatData(user, content, "오후 9시"))
        binding.includedChat.rvChatList.apply {
            adapter.notifyDataSetChanged()
            scrollToPosition(dataList.size - 1)
        }
        binding.includedChat.etChatInput.text = null
    }

    private fun testDrawerUserListed() {
        binding.btnDrawerIntro.setOnClickListener {
            val intent = Intent(this, StoryIntroActivity::class.java)
            startActivity(intent)
        }
        //오늘의 메뉴로 이동
        binding.btnDrawerToday
        profileList = arrayListOf()
        profileList.apply {
            add(TestChatDrawerProfileData(sendUser.nick, sendUser.image))
            add(TestChatDrawerProfileData(receiveUser1.nick, receiveUser1.image))
            add(TestChatDrawerProfileData(receiveUser2.nick, receiveUser2.image))
            add(TestChatDrawerProfileData(receiveUser3.nick, receiveUser3.image))
            add(TestChatDrawerProfileData(receiveUser4.nick, receiveUser4.image))
        }
        val testDataRVAdapter = ChatDrawerRVAdapter(this, profileList)
        binding.rvChatProfileList.adapter = testDataRVAdapter
        binding.rvChatProfileList.layoutManager = LinearLayoutManager(this)
    }

    // 테스트 유저용 초기화
    private fun testUserInit() {
        sendUser = TestUserData("초이", R.drawable.test_choi, Constants.CHAT_MASTER_CODE)
        receiveUser1 = TestUserData("푸", R.drawable.test_poo, Constants.CHAT_GUEST_CODE)
        receiveUser2 = TestUserData("왁", R.drawable.test_wak, Constants.CHAT_GUEST_CODE)
        receiveUser3 = TestUserData("쭈니", R.drawable.test_jooni, Constants.CHAT_GUEST_CODE)
        receiveUser4 = TestUserData("채니", R.drawable.test_chani, Constants.CHAT_GUEST_CODE)
    }

    // 테스트용 채팅 데이터 입력
    private fun setTestData() {
        dataList.apply {
//            add(TestChatData(sendUser, "오늘은 레이아웃 끝내는날", "오후 2시 22분"))
//            add(
//                TestChatData(
//                    receiveUser1,
//                    "내용이 굉장히 길면 어디까지 될까에 대한 궁금증 해결을 위한 예시용 텍스트, 최대 20까지 잡았으나 적용이 잘되나 확인이 필요하다",
//                    "오후 2시 24분"
//                )
//            )
//            add(TestChatData(sendUser, "앞으로 추가해야할 내용은", "오후 2시 42분"))
//            add(
//                TestChatData(
//                    receiveUser1,
//                    "리사이클러뷰에 싱글 셀렉션을 추가해서 각 아이템을 클릭했을때 효과를 부여해야함 간판 수정에서는 구현했지만 앞으로 멀티프로필이나 현재 구현한 내용도 확실한 구현이 아닌 임시방편용 느낌이 강하다 디자이너와 이야기를 해야한다.",
//                    "오후 2시 42분"
//                )
//            )
//            add(TestChatData(sendUser, "끝", "오후 3시 22분"))
//            add(TestChatData(sendUser, "중복 처리를 위한 확인용 텍스트", "오후 3시 40분"))
//            add(TestChatData(receiveUser2, "초이.. 성장에 이르렀는가..?", "오후 9시"))
//            add(TestChatData(sendUser, "오늘은 레이아웃 끝내는날", "오후 2시 22분"))
//            add(
//                TestChatData(
//                    receiveUser1,
//                    "내용이 굉장히 길면 어디까지 될까에 대한 궁금증 해결을 위한 예시용 텍스트, 최대 20까지 잡았으나 적용이 잘되나 확인이 필요하다",
//                    "오후 2시 24분"
//                )
//            )
//            add(TestChatData(sendUser, "앞으로 추가해야할 내용은", "오후 2시 42분"))
//            add(
//                TestChatData(
//                    receiveUser1,
//                    "리사이클러뷰에 싱글 셀렉션을 추가해서 각 아이템을 클릭했을때 효과를 부여해야함 간판 수정에서는 구현했지만 앞으로 멀티프로필이나 현재 구현한 내용도 확실한 구현이 아닌 임시방편용 느낌이 강하다 디자이너와 이야기를 해야한다.",
//                    "오후 2시 42분"
//                )
//            )
//            add(TestChatData(sendUser, "끝", "오후 3시 22분"))
//            add(TestChatData(sendUser, "중복 처리를 위한 확인용 텍스트", "오후 3시 40분"))
//            add(TestChatData(receiveUser2, "초이.. 성장에 이르렀는가..?", "오후 9시"))
        }
    }

    //키보드 내려감
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

}