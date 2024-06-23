package com.example.comfestsea16.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.comfestsea16.R

public open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}