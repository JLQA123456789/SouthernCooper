package com.example.southerncooper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_concentradora1.*

class ActivityConcentradora1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concentradora1)

        btnRemolienda.setOnClickListener{
            val intent = Intent(this, ActivityRemoliendaC1::class.java)
            startActivity(intent)
        }

    }
}