package com.example.amatrace.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
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
    private lateinit var animatorSet: AnimatorSet

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

    override fun onStop() {
        super.onStop()
        if (::animatorSet.isInitialized && animatorSet.isRunning) {
            animatorSet.cancel()
        }
    }

    fun onBackButtonClicked(view: View) {
        onBackPressed()
    }

    private fun setMyButtonEnable() {
        val email = emailEditText.text
        val password = passwordEditText.text
        loginButton.isEnabled = (email != null && email.toString()
            .isNotEmpty()) && (password != null && password.toString().isNotEmpty())
    }

    private fun playAnimation() {
        val titleAnimator = ObjectAnimator.ofFloat(binding.loginTitle, View.ALPHA, 0f, 1f).apply {
            duration = 1000
        }
        val emailInputAnimator = ObjectAnimator.ofFloat(binding.emailInput, View.ALPHA, 0f, 1f).apply {
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
        }
        val passwordInputAnimator = ObjectAnimator.ofFloat(binding.passwordInput, View.ALPHA, 0f, 1f).apply {
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
        }
        val loginButtonAnimator = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).apply {
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
        }
        val forgotPasswordAnimator = ObjectAnimator.ofFloat(binding.forgotPassword, View.ALPHA, 0f, 1f).apply {
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
        }

        animatorSet = AnimatorSet().apply {
            playTogether(titleAnimator, emailInputAnimator, loginButtonAnimator, passwordInputAnimator, forgotPasswordAnimator)
            start()
        }
    }
}
