package com.example.comfestsea16.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.comfestsea16.R
import com.example.comfestsea16.databinding.ActivityRateBinding
import com.example.comfestsea16.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRateBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val serviceType = intent.getStringExtra("service")
        binding.typeOfService.text = serviceType

        fetchAndPopulateUserName()
        setupSubmitButton(serviceType ?: "")
        navigateBackButton()
    }

    private fun navigateBackButton() {
        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupSubmitButton(serviceType: String) { // Add serviceType parameter
        binding.submitButton.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                val nameValue = binding.nameEditText.text.toString().trim()
                val starValue = binding.ratingBar.rating
                val commentValue = binding.commentEditText.text.toString().trim()

                saveReviewToFirestore(userId, nameValue, starValue, commentValue, serviceType) // Pass serviceType
            } else {
                Log.d("RateActivity", "Current user is null.")
            }
        }
    }

    private fun fetchAndPopulateUserName() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val db = FirebaseFirestore.getInstance()

            db.collection("user").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name")
                        // Get reference to the EditText directly
                        val nameEditText = findViewById<EditText>(R.id.nameEditText)
                        nameEditText.setText(name)
                    } else {
                        Log.d("RateActivity", "No user data found in Firestore.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("RateActivity", "Error getting user data: ", exception)
                }
        } else {
            Log.d("RateActivity", "Current user is null.")
        }
    }


    private fun saveReviewToFirestore(
        userId: String,
        name: String,
        star: Float,
        comment: String,
        serviceType: String
    ) {
        val db = FirebaseFirestore.getInstance()
        val reviewId = System.currentTimeMillis().toString()

        val reviewData = hashMapOf(
            "name" to name,
            "rating" to star,
            "comment" to comment,
            "serviceType" to serviceType
        )

        // Store reviews in a subcollection under the user's document
        db.collection("user").document(userId).collection("reviews").document(reviewId)
            .set(reviewData)
            .addOnSuccessListener {
                Log.d("RateActivity", "Review created successfully")
                Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                Log.d("RateActivity", "Error creating review: ", exception)
                Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show()
            }
    }
}
