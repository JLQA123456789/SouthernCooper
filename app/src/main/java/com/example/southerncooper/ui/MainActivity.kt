package com.example.southerncooper.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.southerncooper.util.PreferenceHelper
import kotlinx.android.synthetic.main.activity_main.*
import com.example.southerncooper.util.PreferenceHelper.set
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.io.Response.LoginResponse
import com.example.southerncooper.util.toast
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val snackBar by lazy {  Snackbar.make(mainLayout,
        R.string.press_back_again, Snackbar.LENGTH_SHORT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences=
            PreferenceHelper.defaultPrefs(this)
        if (preferences["session", false]) //Con esto nos asegurmaos que nos es un valor nulo
            goToMenuActivity()

        btnLogin.setOnClickListener {


            performLogin()

        }

        GotoRegister.setOnClickListener {

            Toast.makeText(this, "Por favor completa tus datos", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }



    }

    private fun performLogin(){

        val call = apiService.postLogin(etEmail.text.toString(), etPassword.text.toString())
        call.enqueue(object:Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                toast(t.localizedMessage)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                    if (response.isSuccessful){//{200..300}
                        val loginResponse =response.body()
                        if (loginResponse == null){

                            toast(getString(R.string.error_login_response))
                            return
                        }
                        if (loginResponse.success){

                            createsessionPreferences(loginResponse.jwt)
                            goToMenuActivity()
                        }else{
                            toast(getString(R.string.error_invalid_credentials))

                        }

                    }else{

                        toast(getString(R.string.error_login_response))

                    }

            }

        })

    }

    private fun createsessionPreferences(jwt:String) {

        val preferences =
            PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
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