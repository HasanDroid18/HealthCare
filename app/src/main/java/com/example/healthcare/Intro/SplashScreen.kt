package com.example.healthcare.Intro

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.healthcare.R
import com.example.healthcare.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val SPLASH_SCREEN_DURATION= 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        //Set animation to elements
        binding.imageView.setAnimation(topAnim)
        binding.textView.setAnimation(bottomAnim)
        binding.textView2.setAnimation(bottomAnim)

        lifecycleScope.launch {
            delay(SPLASH_SCREEN_DURATION.toLong())
            startActivity(Intent(this@SplashScreen, BaseActivity::class.java))
            finish()
        }
    }
}