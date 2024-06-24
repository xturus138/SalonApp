package com.example.comfestsea16.fragment.form

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.comfestsea16.databinding.ActivityFormBinding
import com.example.comfestsea16.main.MainActivity

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigateBackButton()
        submitButton()
    }

    private fun navigateBackButton(){
        val backButton = binding.backButton
        backButton.setOnClickListener(){
            val intent =
                Intent(this, MainActivity::class.java )
            startActivity(intent)
        }
    }

    private fun submitButton(){
        val backButton = binding.submitButton
        backButton.setOnClickListener(){
            val intent =
                Intent(this, MainActivity::class.java )
            startActivity(intent)
            Toast.makeText(this,"Anda sudah terdaftar!", Toast.LENGTH_LONG ).show()
        }
    }
}