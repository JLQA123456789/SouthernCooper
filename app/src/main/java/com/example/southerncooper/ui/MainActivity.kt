package com.example.southerncooper.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.io.Response.LoginResponse
import com.example.southerncooper.util.PreferenceHelper
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.util.PreferenceHelper.set
import com.example.southerncooper.util.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val snackBar by lazy {  Snackbar.make(mainLayout,
        R.string.press_back_again, Snackbar.LENGTH_SHORT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        


        val preferences= PreferenceHelper.defaultPrefs(this)
        //Almacenamos el token en preferences
        if (preferences["jwt", ""].contains("."))
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


        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.trim().isEmpty() || password.trim().isEmpty()){
            toast(getString(R.string.error_empty_credentials))
            return
        }

        val call = apiService.postLogin(email, password)
        call.enqueue(object:Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                toast(t.localizedMessage)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                    if (response.isSuccessful){//{200..300}
                        val loginResponse = response.body()
                        if (loginResponse == null){

                            toast(getString(R.string.error_login_response))
                            return
                        }
                        if (loginResponse.success){

                            createsessionPreferences(loginResponse.jwt)
                            toast(getString(R.string.welcome_name, loginResponse.user.name))
                            goToMenuActivity(true)
                        }else{
                            toast(getString(R.string.error_invalid_credentials))

                        }

                    }else{

                        toast(getString(R.string.error_login_response))

                    }

            }

        })

    }
    //Este metodo crea una preferencia porque recibira el token y luego guardar el token....
    private fun createsessionPreferences(jwt: String) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
    }

    private fun goToMenuActivity(isUserInput: Boolean = false) {
        val intent = Intent(
            this,
            ActivityMenuPrincipal::class.java
        )//Se pone .java porque itent es un clase de  java

        if (isUserInput) {
            intent.putExtra("store_token", true)
        }

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