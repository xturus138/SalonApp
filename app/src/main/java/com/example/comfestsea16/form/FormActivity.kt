package com.example.comfestsea16.form

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comfestsea16.databinding.FormLayoutBinding
import com.example.comfestsea16.fragment.booking.SecondFragment
import com.example.comfestsea16.main.MainActivity

class FormActivity : AppCompatActivity() {
    private lateinit var binding: FormLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FormLayoutBinding.inflate(layoutInflater)
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