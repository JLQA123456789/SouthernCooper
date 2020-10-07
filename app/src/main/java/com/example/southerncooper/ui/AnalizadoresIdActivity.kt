package com.example.southerncooper.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.model.Molienda
import com.example.southerncooper.model.Quinta
import com.example.southerncooper.model.Remolienda
import kotlinx.android.synthetic.main.activity_analizadores_id.*
import kotlinx.android.synthetic.main.activity_remolienda_c1.*
import kotlinx.android.synthetic.main.activity_remolienda_c1_card_view_confirmed.*
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

        loadIdMolienda()

        loadIdQuinta()
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


            private fun loadIdMolienda(){

        val call = apiService.getMolienda()
        call.enqueue(object : Callback<ArrayList<Molienda>>{
            override fun onFailure(call: Call<ArrayList<Molienda>>, t: Throwable) {
                Toast.makeText(this@AnalizadoresIdActivity, getString(R.string.error_loading_ids), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<Molienda>>,
                response: Response<ArrayList<Molienda>>
            ) {
                if (response.isSuccessful) {//{200...300}

                    val molienda =response.body()
                    val moliendaoptions = ArrayList<Int>()
                    molienda?.forEach{
                        moliendaoptions.add(it.identify)
                    }

                    MoliendaId.text =  moliendaoptions.toString().replace("[", "").replace("]", "");
                }
            }


        })

    }


    private fun loadIdQuinta(){

        val call = apiService.getQuinta()
        call.enqueue(object : Callback<ArrayList<Quinta>>{
            override fun onFailure(call: Call<ArrayList<Quinta>>, t: Throwable) {
                Toast.makeText(this@AnalizadoresIdActivity, getString(R.string.error_loading_ids), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<Quinta>>,
                response: Response<ArrayList<Quinta>>
            ) {
                if (response.isSuccessful) {//{200...300}

                    val quinta =response.body()
                    val quintaoptions = ArrayList<Int>()
                    quinta?.forEach{
                        quintaoptions.add(it.identify)
                    }

                    QuintaId.text =  quintaoptions.toString().replace("[", "").replace("]", "");
                }
            }


        })

    }



}