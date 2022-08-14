package com.vanclykin.bulletinboard.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vanclykin.bulletinboard.R
import com.vanclykin.bulletinboard.databinding.ActivityEditAdsBinding
import com.vanclykin.bulletinboard.databinding.ActivityMainBinding

class EditAdsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}