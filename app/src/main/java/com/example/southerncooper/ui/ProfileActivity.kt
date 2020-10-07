package com.example.southerncooper.ui
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.model.User
import com.example.southerncooper.util.PreferenceHelper
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.util.toast
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    private  val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val jwt = preferences["jwt", ""]
        val authHeader =  "Bearer $jwt"
        val call = apiService.getUser(authHeader)

        call.enqueue(object: Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                toast(t.localizedMessage)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){

                    val user = response.body()

                    if(user != null)
                        displayProfile(user)
                }
            }

        })
/*
        Handler().postDelayed({
                displayProfile()
        },3000)*/
    }

    private fun displayProfile(user: User){

        etName.setText(user.name)
        etPhone.setText(user.phone)
        etArea.setText(user.area)

        ProgressbarProfile.visibility = View.GONE
        linearLayoutProfile.visibility = View.VISIBLE

        btnConfirmProfile.setOnClickListener{
            saveProfile()
        }
    }

    private fun saveProfile(){



        val name = etName.text.toString()
        val phone = etPhone.text.toString()
        val area = etArea.text.toString()

        if (name.length < 4 ){
            layoutname.error = getString(R.string.error_profile_name)
            return
        }

        val jwt = preferences["jwt", ""]
        val authHeader =  "Bearer $jwt"

        val call = apiService.postUser(authHeader, name, phone, area)
        call.enqueue(object: Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                if (response.isSuccessful){
                    toast(getString(R.string.profile_success_message))
                    finish()
                }
            }


        })

    }
}