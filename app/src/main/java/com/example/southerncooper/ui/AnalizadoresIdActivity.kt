package com.example.southerncooper.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.model.Remolienda
import kotlinx.android.synthetic.main.activity_analizadores_id.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Array

class AnalizadoresIdActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analizadores_id)


        btnprevius.setOnClickListener{
            val intent = Intent(this, ActivityConcentradora1::class.java)
            startActivity(intent)
        }

        loadIdRemolienda()
    }

        private fun loadIdRemolienda(){

            val call = apiService.getRemolienda()
            call.enqueue(object : Callback<ArrayList<Remolienda>>{
                override fun onFailure(call: Call<ArrayList<Remolienda>>, t: Throwable) {
                    Toast.makeText(this@AnalizadoresIdActivity, getString(R.string.error_loading_ids), Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onResponse(
                    call: Call<ArrayList<Remolienda>>,
                    response: Response<ArrayList<Remolienda>>
                ) {
                    if (response.isSuccessful) {//{200...300}

                        val remolienda =response.body()
                        val remoliendaoptions = ArrayList<Int>()
                        remolienda?.forEach{
                            remoliendaoptions.add(it.identify)
                        }

                        RemoliendaId.text =  remoliendaoptions.toString().replace("[", "").replace("]", "");
                    }
                }


            })

        }
}