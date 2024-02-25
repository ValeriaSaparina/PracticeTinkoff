package com.example.travels

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travels.ui.fragments.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var testFA: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, SignInFragment())
            .commit()

        testFA = Firebase.auth
    }
}