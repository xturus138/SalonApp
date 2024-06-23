package com.example.comfestsea16.support

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.comfestsea16.databinding.CustomerSupportBinding
import com.example.comfestsea16.main.MainActivity

class CustomerSupportActivity : AppCompatActivity() {
    private lateinit var binding: CustomerSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomerSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigateBackButton()
    }

    private fun navigateBackButton(){
        val backButton = binding.backButton
        backButton.setOnClickListener(){
            val intent =
                Intent(this, MainActivity::class.java )
            startActivity(intent)
        }
    }
}
