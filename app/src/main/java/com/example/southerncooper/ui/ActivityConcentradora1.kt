package com.example.southerncooper.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.southerncooper.R
import kotlinx.android.synthetic.main.activity_concentradora1.*

class ActivityConcentradora1 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concentradora1)

        btnRemolienda.setOnClickListener{
            val intent = Intent(this, ActivityRemoliendaC1::class.java)
            startActivity(intent)
        }

        btnAnalizadores.setOnClickListener{
            val intent = Intent(this, AnalizadoresIdActivity::class.java)
            startActivity(intent)
        }

        btnMolienda.setOnClickListener{
            val intent = Intent(this, ActivityMoliendaC1::class.java)
            startActivity(intent)
        }

        btnQuinta.setOnClickListener{
            val intent = Intent(this, ActivityQuintaC1::class.java)
            startActivity(intent)
        }

        btnNorte.setOnClickListener{
            val intent = Intent(this, ActivityNorte::class.java)
            startActivity(intent)
        }
        btnMoly.setOnClickListener{
            val intent = Intent(this, ActivityMoly::class.java)
            startActivity(intent)
        }

    }




}