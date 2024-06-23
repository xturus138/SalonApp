package com.example.comfestsea16.Authentication.Login

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.comfestsea16.Authentication.Register.RegisterActivity
import com.example.comfestsea16.Main.MainActivity
import com.example.comfestsea16.R
import com.example.comfestsea16.databinding.ActivityLoginBinding
import com.example.comfestsea16.databinding.ActivityRegisterBinding
import com.example.comfestsea16.databinding.LoginLayoutBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent =
                Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val email = binding.emailLogin
        val password = binding.passwordLogin
        val loginButton = binding.loginButton
        val progressBar = binding.progressBar
        val regisButton = binding.regisButton

        regisButton.setOnClickListener() {
            val intent =
                Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener() {
            var emailText = email.text.toString()
            var passwordText = password.text.toString()

            if (emailText.isEmpty()) {
                email.error = "Email is required"
            } else if (passwordText.isEmpty()) {
                password.error = "Password Required"
            } else {
                progressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            Toast.makeText(
                                baseContext,
                                "Login Succsess.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            progressBar.visibility = View.GONE
                            val user = auth.currentUser
                            val intent =
                                Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }




        }

    }
}