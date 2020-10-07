package com.example.southerncooper.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.southerncooper.R
import kotlinx.android.synthetic.main.activity_concentradora2.*

class ActivityConcentradora2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concentradora2)



        btnRemolienda2.setOnClickListener{
            val intent = Intent(this, ActivityRemoliendaC2::class.java)
            startActivity(intent)
        }

        btnAnalizadores2.setOnClickListener{
            val intent = Intent(this, AnalizadoresIdActivityC2::class.java)
            startActivity(intent)
        }

        btnMolienda2.setOnClickListener{
            val intent = Intent(this, ActivityMolienda2::class.java)
            startActivity(intent)
        }

        btnCobre.setOnClickListener{
            val intent = Intent(this, ActivityCobre::class.java)
            startActivity(intent)
        }




    }
}