package com.example.amatrace

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amatrace.ui.LoginActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inisialisasi tombol login
        val loginButton: Button = findViewById(R.id.loginButton)

        // Set onClickListener untuk tombol login
        loginButton.setOnClickListener {
            // Buat intent untuk memulai ActivityLogin
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
