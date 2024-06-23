package com.example.comfestsea16.Activity

import android.os.Bundle
import com.example.comfestsea16.databinding.ActivityIntroBinding
import com.example.comfestsea16.databinding.ActivityMainBinding


class IntroActivity : BaseActivity()  {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setVariable();
    }

    private fun setVariable() {
        TODO("Not yet implemented")
    }

}