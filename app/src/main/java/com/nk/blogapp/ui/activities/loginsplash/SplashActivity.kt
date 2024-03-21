package com.nk.blogapp.ui.activities.loginsplash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nk.blogapp.R
import com.nk.blogapp.ui.activities.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val splashTime: Long = 1000
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_splash)

        val user = Firebase.auth.currentUser

        // Use CoroutineScope to delay the splash screen
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashTime)
            if (user != null) {
                // User is signed in
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                // No user is signed in
                startActivity(Intent(this@SplashActivity, LoginRegisterActivity::class.java))
            }
            finish()
        }
    }
}