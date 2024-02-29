package com.nk.blogapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nk.blogapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val splashTime: Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Use CoroutineScope to delay the splash screen
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashTime)
            startActivity(Intent(this@SplashActivity, LoginRegisterActivity::class.java))
            finish()
        }
    }
}