package com.example.healthcare.Intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.healthcare.MainActivity
import com.example.healthcare.R
import com.google.firebase.auth.FirebaseAuth

class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is signed in, launch MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Finish BaseActivity to prevent going back to it
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            findNavController(R.id.intro_container).navigate(R.id.loginFragment)
        }
    }

}
