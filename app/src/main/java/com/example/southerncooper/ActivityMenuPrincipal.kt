package com.example.southerncooper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu_principal.*
import com.example.southerncooper.PreferenceHelper.set


class ActivityMenuPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        btnConcentradora1.setOnClickListener{

            val intent = Intent(this, ActivityConcentradora1::class.java)
            startActivity(intent)

        }

        btnLogout.setOnClickListener{
            clearsessionPreferences()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun clearsessionPreferences(){
        val preferences= PreferenceHelper.defaultPrefs(this)
        preferences["session"] = false
    }
}