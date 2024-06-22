package com.example.comfestsea16

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.comfestsea16.databinding.CustomerSupportBinding

class CustomerSupportActivity : AppCompatActivity() {
    private lateinit var binding: CustomerSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomerSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
