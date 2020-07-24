package com.example.southerncooper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.example.southerncooper.PreferenceHelper.set
import com.example.southerncooper.PreferenceHelper.get
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {


    private val snackBar by lazy {  Snackbar.make(mainLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences= PreferenceHelper.defaultPrefs(this)
        if (preferences["session", false]) //Con esto nos asegurmaos que nos es un valor nulo
            goToMenuActivity()

        btnLogin.setOnClickListener {

            createsessionPreferences()
            goToMenuActivity()
        }

        GotoRegister.setOnClickListener {

            Toast.makeText(this, "Por favor completa tus datos", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }

    }


    private fun createsessionPreferences() {

        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["session"] = true
    }

    private fun goToMenuActivity() {

        val intent = Intent(
            this,
            ActivityMenuPrincipal::class.java
        )//Se pone .java porque itent es un clase de  java
        startActivity(intent)
        finish()
    }

    override fun onBackPressed(){

        if(snackBar.isShown)
            super.onBackPressed()
        //SI no pues  que se muestre
        else
            snackBar.show()

    }
}