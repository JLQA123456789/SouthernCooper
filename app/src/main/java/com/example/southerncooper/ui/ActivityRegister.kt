package com.example.southerncooper.ui

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.io.Response.LoginResponse
import com.example.southerncooper.util.PreferenceHelper
import com.example.southerncooper.util.PreferenceHelper.set
import com.example.southerncooper.util.toast
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityRegister : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvGotoLogin.setOnClickListener {
            val intent = Intent(this , MainActivity::class.java )
           startActivity(intent)
        }



        RegisterConfirmation.setOnClickListener{
            perfomRegister()
        }

    }

    private fun perfomRegister(){

        val name = etNameRegister.text.toString().trim()
        val email = etEmailRegister.text.toString().trim()
        val password = etPasswordRegister.text.toString()
        val passwordConfirmation = etPasswordConfirmation.text.toString()
        val phone = etPhoneRegister.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()  || phone.isEmpty()){
            toast(getString(R.string.complete_register_now))
            return
        }
        if (password != passwordConfirmation){
            toast(getString(R.string.error_password_register))
            return
        }

        val call = apiService.postRegister(name, email, password, passwordConfirmation, phone)
        call.enqueue(object: Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                toast(t.localizedMessage)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful){

                        val loginResponse = response.body()
                    if (loginResponse == null){
                        toast(getString(R.string.error_response_server))
                        return
                    }
                    if (loginResponse.success){

                        createsessionPreferences(loginResponse.jwt)
                        toast(getString(R.string.welcome_name, loginResponse.user.name))
                        goToMenuActivity()
                    }else{
                        toast(getString(R.string.error_invalid_credentials))
                    }
                }else{
                    toast(getString(R.string.error_register_validation))
                }
            }


        })
    }

    private fun createsessionPreferences(jwt: String){

        val preferences= PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
    }

    private fun goToMenuActivity(){

        val intent = Intent(this, ActivityMenuPrincipal::class.java)//Se pone .java porque itent es un clase de  java
        startActivity(intent)
        finish()
    }
}