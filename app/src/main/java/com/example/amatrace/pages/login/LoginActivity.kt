package com.example.amatrace.pages.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.amatrace.MainActivity
import com.example.amatrace.databinding.ActivityLoginBinding
import com.example.amatrace.pages.customview.EmailEditText
import com.example.amatrace.pages.customview.LoginButton
import com.example.amatrace.pages.customview.PasswordEditText
import com.example.amatrace.pages.landing.LandingActivity
import com.example.amatrace.pages.lupaPassword.lupaPasswordActivity
import com.example.amatrace.pages.producer.ProducerMainActivity
import com.example.amatrace.pages.supplier.MainSupplierActivity

import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.LoginResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var myPreference: Preference
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
        myPreference = Preference(this)

        showLoading(false)

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

        loginButton.setOnClickListener {
            login(emailEditText.text.toString(), passwordEditText.text.toString())
            showLoading(true)
        }

        // Cek status login berdasarkan peran pengguna (role)
        if (myPreference.getStatusLogin()) {
            val account = myPreference.getAccountInfo()
            account?.let {
                val intent = when (it.role) {
                    "supplier" -> Intent(this@LoginActivity, MainSupplierActivity::class.java)
                    "warehouse" -> Intent(this@LoginActivity, MainSupplierActivity::class.java)
                    "producer" -> Intent(this@LoginActivity, ProducerMainActivity::class.java)
                    else -> Intent(this@LoginActivity, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }
    }



    override fun onStop() {
        super.onStop()
        if (::animatorSet.isInitialized && animatorSet.isRunning) {
            animatorSet.cancel()
        }
    }

    fun onBackButtonClicked(view: View) {
        intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
    }

    private fun setMyButtonEnable() {
        val email = emailEditText.text
        val password = passwordEditText.text
        loginButton.isEnabled = (email != null && email.toString()
            .isNotEmpty()) && (password != null && password.toString().isNotEmpty())
    }


    private fun login(email: String, password: String) {
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)

        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val call = Config.getApiService().loginUser(requestBody)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        // Login berhasil, simpan token dan data akun ke dalam SharedPreferences
                        val loginData = loginResponse.data
                        if (loginData != null) {
                            myPreference.setAccessToken(loginData.accessToken)
                            myPreference.setStatusLogin(true)
                            myPreference.saveAccountInfo(loginData.account)

                            // Pindah ke halaman sesuai dengan peran pengguna (role)
                            val intent = when (loginData.account.role) {
                                "supplier" -> Intent(this@LoginActivity, MainSupplierActivity::class.java)
                                "warehouse" -> Intent(this@LoginActivity, MainSupplierActivity::class.java)
                                "producer" -> Intent(this@LoginActivity, ProducerMainActivity::class.java)
                                else -> Intent(this@LoginActivity, MainActivity::class.java)
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            // Jika data loginResponse null
                            Toast.makeText(this@LoginActivity, "Login data is null", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Login gagal, tampilkan pesan dari server
                        val message = loginResponse?.message ?: "Login failed."
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Gagal melakukan permintaan ke server, tampilkan pesan error HTTP
                    Toast.makeText(this@LoginActivity, "HTTP Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Gagal melakukan permintaan ke server, tampilkan pesan error
                Toast.makeText(this@LoginActivity, "Login failed. Please check your internet connection.", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        })
    }




    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
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
