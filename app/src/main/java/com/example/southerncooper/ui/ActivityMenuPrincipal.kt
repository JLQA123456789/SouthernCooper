@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.southerncooper.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.util.PreferenceHelper
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.util.PreferenceHelper.set
import com.example.southerncooper.util.toast
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_menu_principal.*
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

        val storetoken = intent.getBooleanExtra("store_token", false)
        if(storetoken)
            storetoken()

        btnConcentradora1.setOnClickListener{

            val intent = Intent(this, ActivityConcentradora1::class.java)
            startActivity(intent)

        }

        btnConcentradora2.setOnClickListener{

            val intent = Intent(this, ActivityConcentradora2::class.java)
            startActivity(intent)

        }

        btnLogout.setOnClickListener{
            perfomLogout()

        }
    }

        private fun storetoken(){
            val jwt  = preferences["jwt",""]
            val authHeader = "Bearer $jwt "

            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this){ instanceIdResult ->
                val deviceToken = instanceIdResult.token
                //Log.d("FCMService", deviceToken)
               val call = apiService.postToken(authHeader, deviceToken)
                call.enqueue(object:Callback<Void>{
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        toast(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful){
                            Log.d(Companion.TAG,"Token registrado correctamente")
                        }else{
                            Log.d(Companion.TAG, "Hubo un problema al registrar el token")
                        }
                    }

                })
            }

        }


        fun editProfile(view:View){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
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


    override fun onBackPressed() {
            MenuPrincipal.visibility== View.VISIBLE
    }

    companion object {
        private const val TAG = "ActivityMenuPrincipal"
    }
}