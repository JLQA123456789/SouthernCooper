package com.example.southerncooper.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.model.MoliendaC2
import com.example.southerncooper.model.RemoliendaC2
import kotlinx.android.synthetic.main.activity_analizadores_id.btnprevius
import kotlinx.android.synthetic.main.activity_analizadores_id_c2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalizadoresIdActivityC2 : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analizadores_id_c2)


        btnprevius.setOnClickListener{
            val intent = Intent(this, ActivityConcentradora2::class.java)
            startActivity(intent)
        }

        loadIdRemoliendaC2()
        loadIdMoliendaC2()


    }

    private fun loadIdRemoliendaC2(){

        val call = apiService.getRemoliendaC2()
        call.enqueue(object : Callback<ArrayList<RemoliendaC2>> {
            override fun onFailure(call: Call<ArrayList<RemoliendaC2>>, t: Throwable) {
                Toast.makeText(this@AnalizadoresIdActivityC2, getString(R.string.error_loading_ids), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<RemoliendaC2>>,
                response: Response<ArrayList<RemoliendaC2>>
            ) {
                if (response.isSuccessful) {//{200...300}

                    val remoliendac2 =response.body()
                    val remoliendac2options = ArrayList<Int>()
                    remoliendac2?.forEach{
                        remoliendac2options.add(it.identify)
                    }

                    RemoliendaIdC2.text =  remoliendac2options.toString().replace("[", "").replace("]", "");
                }
            }


        })

    }


    private fun loadIdMoliendaC2(){

        val call = apiService.getMoliendaC2()
        call.enqueue(object : Callback<ArrayList<MoliendaC2>> {
            override fun onFailure(call: Call<ArrayList<MoliendaC2>>, t: Throwable) {
                Toast.makeText(this@AnalizadoresIdActivityC2, getString(R.string.error_loading_ids), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<MoliendaC2>>,
                response: Response<ArrayList<MoliendaC2>>
            ) {
                if (response.isSuccessful) {//{200...300}

                    val moliendac2 =response.body()
                    val moliendac2options = ArrayList<Int>()
                    moliendac2?.forEach{
                        moliendac2options.add(it.identify)
                    }

                    MoliendaIdC2.text =  moliendac2options.toString().replace("[", "").replace("]", "");
                }
            }


        })

    }


}
