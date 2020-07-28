@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.southerncooper.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.southerncooper.util.PreferenceHelper
import kotlinx.android.synthetic.main.activity_menu_principal.*
import com.example.southerncooper.util.PreferenceHelper.set
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityMenuPrincipal : AppCompatActivity() {


    private  val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        btnConcentradora1.setOnClickListener{

            val intent = Intent(this, ActivityConcentradora1::class.java)
            startActivity(intent)

        }

        btnLogout.setOnClickListener{
            perfomLogout()

        }
    }

        private fun perfomLogout(){
        //Obtenemos el JWT desde las preferencias para cerrar session
            val jwt = preferences["jwt", ""]
            val call = apiService.postLogout("Bearer $jwt" )
            call.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    toast(t.localizedMessage)
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    //Limpiar el token
                    clearsessionPreferences()
                    val intent = Intent(this@ActivityMenuPrincipal, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        }

    private fun clearsessionPreferences(){
        preferences["jwt"] =""
    }
}