package com.example.simya.src.main.splash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.example.simya.util.Constants
import com.example.simya.src.main.login.LoginActivity
import com.example.simya.util.data.UserData
import com.example.simya.databinding.ActivitySplashBinding
import com.example.simya.util.sharedpreferences.Shared

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private val splashHandler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onShared()
        init()
    }


    //    )
    /*  달이 밝게 비춤 (시작)
    **  달이 이동하면서 별, 도시들이 나타남
    **  불이켜지면서 심야식당 로고 등장
    **  심야식당 인트로 등장
    **  로고 + 인트로 빼고 페이드 아웃 */
    private fun init() {
//    **    달이 밝게 비춤 (시작)
//    **    달이 이동하면서 별, 도시들이 나타남
        splashHandler.postDelayed(
            Runnable { sequenceOne() }, 1000
        )
        splashHandler.postDelayed(
            Runnable { sequenceTwo() }, 2000
        )
        splashHandler.postDelayed(
            Runnable { sequenceThree() }, 3000
        )
        splashHandler.postDelayed(
            Runnable { sequenceFour() }, 4000
        )


        splashHandler.postDelayed(
            Runnable { moveToHome() }, 16000
        )
    }

    private fun zoomOut(imageView: ImageView) {
        ObjectAnimator.ofFloat(imageView, View.SCALE_X, .5F).setDuration(1000).start()
        ObjectAnimator.ofFloat(imageView, View.SCALE_Y, .5F).setDuration(1000).start()
    }

    private fun moveMoon(imageView: ImageView) {
        ObjectAnimator.ofFloat(
            imageView,
            View.TRANSLATION_X,
            (binding.ivStars.x - binding.ivMoon.x)
        ).setDuration(1000).start()
        ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, binding.ivStars.y - binding.ivMoon.y)
            .setDuration(1000).start()
    }
    // 페이드인 애니메이션
    private fun fadeIn(imageView: ImageView) {
        imageView.isInvisible = false
        val fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f)
        fadeIn.duration = 1000
        fadeIn.start()
    }
    // 페이드 아웃 애니메이션
    private fun fadeOut(imageView: ImageView) {
        val fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f)
        fadeOut.duration = 1500
        fadeOut.start()
        fadeOut.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                imageView.isInvisible = true
            }

            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationRepeat(p0: Animator?) {}

        })
    }

    // 스플래쉬 시퀀스
    private fun sequenceOne() {
        zoomOut(binding.ivMoon)
        moveMoon(binding.ivMoon)
        fadeIn(binding.ivStars)
        fadeIn(binding.ivBuildingOff)

    }

    private fun sequenceTwo() {
        fadeIn(binding.ivBuildingOn)
        fadeIn(binding.ivLogo)
    }

    private fun sequenceThree() {
        fadeIn(binding.ivIntro)
    }

    private fun sequenceFour() {
        fadeOut(binding.ivMoon)
        fadeOut(binding.ivBuildingOff)
        fadeOut(binding.ivStars)
        fadeOut(binding.ivBuildingOn)
    }

    // 홈으로 이동
    private fun moveToHome() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // SharedPreferences
    private fun onShared() {
        UserData.init(
            Shared.prefs.getString("accessToken", Constants.DEFAULT),
            Shared.prefs.getString("refreshToken", Constants.DEFAULT)
        )
        Log.d("User AccessToken", UserData.getUserAccessToken())
        Log.d("User RefreshToken", UserData.getUserRefreshToken())
    }

    // 테스트용 터치 이벤트
    override fun onTouchEvent(event: MotionEvent): Boolean {
        splashHandler.removeCallbacksAndMessages(null)
        moveToHome()
        return true
    }
}