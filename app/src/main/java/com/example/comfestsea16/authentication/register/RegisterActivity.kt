// RegisterActivity.kt
package com.example.comfestsea16.authentication.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.comfestsea16.authentication.login.LoginActivity
import com.example.comfestsea16.databinding.ActivityRegisterBinding
import com.example.comfestsea16.main.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference
    private val registerViewModel: RegisterViewModel by viewModels()
    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: FirebaseUser

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = registerViewModel.getCurrentUser()
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullName = binding.fullName
        val email = binding.email
        val phoneNumber = binding.phoneNumber
        val password = binding.password
        val regisButton = binding.registerButton
        val backButton = binding.backButtonRegis
        val progressBar = binding.progressBar

        backButton.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        regisButton.setOnClickListener {
            Log.d(TAG, "onCreate: REGISTER BUTTON CLICKED!!!!!")
            val fullNameText = fullName.text.toString().trim()
            val emailText = email.text.toString().trim()
            val phoneNumberText = phoneNumber.text.toString().trim()
            val passwordText = password.text.toString()

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
                registerViewModel.register(emailText, passwordText) // Initiate registration

                registerViewModel.registerResult.observe(this, Observer { user ->
                    progressBar.visibility = View.GONE
                    if (user != null) {
                        // Registration successful
                        val userId = user.uid // Capture the uid of the newly created user
                        saveUserDataToFirestore(userId, fullNameText, phoneNumberText)

                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(baseContext, "Account Created.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure")
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                })



            }

        }



        registerViewModel.registerResult.observe(this, Observer { user ->
            progressBar.visibility = View.GONE
            if (user != null) {
                Log.d(TAG, "createUserWithEmail:success")
                Toast.makeText(baseContext, "Account Created.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.w(TAG, "createUserWithEmail:failure")
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserDataToFirestore(userId: String, fullName: String, phoneNumber: String) {
        val db = FirebaseFirestore.getInstance()
        val userMap = hashMapOf(
            "name" to fullName,
            "number" to phoneNumber
        )

        db.collection("user").document(userId).set(userMap)
            .addOnSuccessListener {
                Log.d(TAG, "onCreate: Data Created: Database Added Firestore")
            }
            .addOnFailureListener {
                Log.d(TAG, "onCreate: Data Created: Database Error Firestore")
            }
    }

}
