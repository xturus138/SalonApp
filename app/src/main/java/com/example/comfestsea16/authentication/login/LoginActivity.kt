package com.example.comfestsea16.authentication.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.comfestsea16.activity.AdminActivity
import com.example.comfestsea16.authentication.register.RegisterActivity
import com.example.comfestsea16.databinding.ActivityLoginBinding
import com.example.comfestsea16.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.emailLogin
        val password = binding.passwordLogin
        val loginButton = binding.loginButton
        val progressBar = binding.progressBar
        val regisButton = binding.regisButton

        regisButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (emailText.isEmpty()) {
                email.error = "Email is required"
            } else if (passwordText.isEmpty()) {
                password.error = "Password Required"
            } else {
                progressBar.visibility = View.VISIBLE
                loginViewModel.login(emailText, passwordText)
            }
        }

        loginViewModel.loginResult.observe(this, Observer { user ->
            progressBar.visibility = View.GONE
            if (user != null) {
                Log.d(TAG, "signInWithEmail:success")
                Toast.makeText(baseContext, "Login Success.", Toast.LENGTH_SHORT).show()
                if (email.text.toString() == "thomas.n@compfest.id" && password.text.toString() == "Admin123") {
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                finish()
            } else {
                Log.w(TAG, "signInWithEmail:failure")
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
