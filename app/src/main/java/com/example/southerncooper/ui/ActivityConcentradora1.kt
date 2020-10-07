package com.example.southerncooper.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.southerncooper.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_concentradora1.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_norte.*

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


    }




}