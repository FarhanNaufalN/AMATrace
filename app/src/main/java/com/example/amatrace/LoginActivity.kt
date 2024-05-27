package com.example.amatrace

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.amatrace.databinding.ActivityLoginBinding
import com.example.amatrace.ui.customview.EmailEditText
import com.example.amatrace.ui.customview.LoginButton
import com.example.amatrace.ui.customview.PasswordEditText
import com.example.amatrace.ui.lupaPassword.lupaPasswordActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: LoginButton
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var errorPassword: TextView
    private lateinit var forgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginButton = binding.loginButton
        emailEditText = binding.emailInput
        passwordEditText = binding.passwordInput
        errorPassword = binding.errorPassword
        forgotPassword = binding.forgotPassword

        playAnimation()
        setMyButtonEnable()
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        passwordEditText.bindTextView(errorPassword)


        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length >= 8) setMyButtonEnable() else loginButton.isEnabled =
                    false
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        forgotPassword.setOnClickListener{
            val intent = Intent(this, lupaPasswordActivity::class.java)
            startActivity(intent)
        }


    }

    private fun setMyButtonEnable() {
        val email = emailEditText.text
        val password = passwordEditText.text
        loginButton.isEnabled = (email != null && email.toString()
            .isNotEmpty()) && (password != null && password.toString().isNotEmpty())
    }

    private fun playAnimation() {

        val email = ObjectAnimator.ofFloat(binding.emailInput, View.ALPHA, 1f).setDuration(5000)
        val password = ObjectAnimator.ofFloat(binding.passwordInput, View.ALPHA, 1f).setDuration(5000)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(5000)

        AnimatorSet().apply {
            playSequentially(
                email,
                password,
                loginButton,
            )
            start()
        }
    }
}
