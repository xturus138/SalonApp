package com.example.comfestsea16.Authentication.Register

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.comfestsea16.Authentication.Login.LoginActivity
import com.example.comfestsea16.Main.MainActivity
import com.example.comfestsea16.R
import com.example.comfestsea16.databinding.ActivityFormBinding
import com.example.comfestsea16.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
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
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val fullName = binding.fullName
        val email = binding.email
        val phoneNumber = binding.phoneNumber
        val password = binding.password
        val regisButton = binding.registerButton
        val progressBar = binding.progressBar

        regisButton.setOnClickListener() {
            var fullNameText = fullName.text.toString()
            var emailText = email.text.toString()
            var phoneNumberText = phoneNumber.text.toString()
            var passwordText = password.text.toString()

            if (fullNameText.isEmpty()) {
                fullName.error = "Full name is required"
            } else if (emailText.isEmpty()) {
                email.error = "Email is required"
            } else if (phoneNumberText.isEmpty()) {
                phoneNumber.error = "Phone number is required"
            } else if (passwordText.isEmpty()) {
                password.error = "Password is required"
            } else {
                progressBar.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                baseContext,
                                "Account Created.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val user = auth.currentUser
                            val intent =
                                Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            progressBar.visibility = View.GONE
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