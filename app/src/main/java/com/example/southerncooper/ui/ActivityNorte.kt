package com.example.southerncooper.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.io.Response.SimpleResponse
import com.example.southerncooper.util.PreferenceHelper
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.util.toast
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.activity_concentradora2.*
import kotlinx.android.synthetic.main.activity_norte.*
import kotlinx.android.synthetic.main.activity_remolienda_c1.*
import kotlinx.android.synthetic.main.activity_remolienda_c1_card_view_confirmed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ActivityNorte : AppCompatActivity() {


    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    var descriptionData =
        arrayOf("M1", "M2", "M3", "Ok")

    private val selectedCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_norte)


        val stateProgressBar = findViewById(R.id.progress) as StateProgressBar
        stateProgressBar.setStateDescriptionData(descriptionData)


        button.setOnClickListener {


            when (stateProgressBar.getCurrentStateNumber()) {
                1 -> {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    Step1_norte.visibility = View.GONE
                    Step2_norte.visibility = View.VISIBLE
                    etName_norte.error = getString(R.string.validate_name_description)
                    etDate_norte.error = getString(R.string.validate_remolienda_date)
                    etTime_norte.error = getString(R.string.validate_remolienda_time)

                }
                2 -> {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                    Step2_norte.visibility = View.GONE
                    Step3_norte.visibility = View.VISIBLE
                    }

                3 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)

                    Step3_norte.visibility = View.GONE
                    Step4_norte.visibility = View.VISIBLE

                }

                else -> { // Note the block

                    if (btnConfirmedNorte.isClickable == true ){

                        toast("Lo siento pero no guardaste")
                        stateProgressBar.setAllStatesCompleted(false)
                        Step4_norte.visibility = View.VISIBLE
                        Step5_norte.visibility = View.GONE
                    } else{
                        stateProgressBar.setAllStatesCompleted(true)
                        Step4_norte.visibility = View.GONE
                        Step5_norte.visibility = View.VISIBLE
                        button.visibility = View.GONE
                        button2.visibility = View.GONE
                    }

                }
            }




        }

        button2.setOnClickListener {

            when (stateProgressBar.getCurrentStateNumber()) {
                1 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    Step3_norte.visibility = View.GONE
                    Step2_norte.visibility = View.VISIBLE
                     }

                2 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

                    Step2_norte.visibility = View.GONE
                    Step1_norte.visibility = View.VISIBLE
                     }
                3 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    Step3_norte.visibility = View.GONE
                    Step2_norte.visibility = View.VISIBLE
                }


                else ->  {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)

                    Step4_norte.visibility = View.GONE
                    Step3_norte.visibility = View.VISIBLE

                }
            }

        }


        btnConfirmedNorte.setOnClickListener{

            performStoreRemolienda()
        }

        btnConfirmedNorte2.setOnClickListener{

            performStoreRemolienda()
        }

        btnConfirmedNorte3.setOnClickListener {

            performStoreRemolienda()

        }
        btnRegresarNorte.setOnClickListener{
            finish()
        }


        val MuestraOptions = arrayOf(" Reb.Hidro.L1", " Reb.Hidro.L2", "Relave.Final", "Rela.Agot.L1", "Rela.Agot.L2", "Alimt.1ra.Limp", "Conc.1ra.Limp.L1", "Conc.1ra.Limp.L2", "Conc.Colect", "Rel.2da.Limp.L1","Rel.2da.Limp.L2" , "Conc.Prim.L2" , "Conc.Prim.L1")
        spinnerMuestra_norte.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MuestraOptions)

        val Muestra1Options = arrayOf(" Reb.Hidro.L1", " Reb.Hidro.L2", "Relave.Final", "Rela.Agot.L1", "Rela.Agot.L2", "Alimt.1ra.Limp", "Conc.1ra.Limp.L1", "Conc.1ra.Limp.L2", "Conc.Colect", "Rel.2da.Limp.L1","Rel.2da.Limp.L2" , "Conc.Prim.L2" , "Conc.Prim.L1")
        spinnerMuestra2_norte.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra1Options)

        val Muestra2Options = arrayOf(" Reb.Hidro.L1", " Reb.Hidro.L2", "Relave.Final", "Rela.Agot.L1", "Rela.Agot.L2", "Alimt.1ra.Limp", "Conc.1ra.Limp.L1", "Conc.1ra.Limp.L2", "Conc.Colect", "Rel.2da.Limp.L1","Rel.2da.Limp.L2" , "Conc.Prim.L2" , "Conc.Prim.L1")
        spinnerMuestra3_norte.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra2Options)





    }


    fun onClickDate(v: View?) {

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->

            // Toast.makeText(this,"$y-$m-$d", Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y, m, d)
            etDate_norte.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    (m+1).twoDigits(),
                    d.twoDigits()
                )
            )
            etDate_norte.error = null
        }

        // new dialogo
        val datePickerDialog = DatePickerDialog(this, listener, year, month, dayOfMonth)

        // set  limits
        val datePicker = datePickerDialog.datePicker
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 0)
        datePicker.minDate = calendar.timeInMillis//+1 now

        // show dialogo
        datePickerDialog.show()
    }

    private fun Int.twoDigits()
            = if(this >= 10) this.toString() else "0$this"




    override fun onBackPressed(){


        when {
            Step4_norte.visibility== View.VISIBLE -> {
                SalirStep()


            }
            Step3_norte.visibility == View.VISIBLE -> {
                SalirStep()

            }
            Step2_norte.visibility == View.VISIBLE -> {
                SalirStep()

            }

            Step1_norte.visibility == View.VISIBLE -> {
                SalirStep()

            }
        }
    }

    private fun SalirStep(){

        //Esta clase Builder nos facilitara la construccion de un dialogo
        val builder= AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.dialog_create_muestra_exit_title))
        builder.setMessage(getString(R.string.dialog_create_muestra_exit_message))
        builder.setPositiveButton(getString(R.string.dialog_create_muestra_exit_positive_btn)) { _, _ ->
            //Esto dos parametros representan al dialogo omo tal y el boton sobre el cual d¿se hizo click y los renomberamos con quiones bajos
            finish()
        }
        builder.setNegativeButton(getString(R.string.dialog_create_muestra_negative_btn)) { dialog, _ ->
            //osea si se desea continua con el registro se ocualta dialog
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
        //El builder nos cirbe para construir
    }

    private fun performStoreRemolienda(){

        if (btnConfirmedNorte.isClickable){

            btnConfirmedNorte.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_norte.text.toString()
            val date = etDate_norte.text.toString()
            val muestra =  spinnerMuestra_norte.selectedItem.toString()
            val hora = etTime_norte.text.toString()
            val identify = etID_norte.text.toString()
            val codigo = etCodigo_norte.text.toString()



            val call = apiService.storeNorte(
                authHeader, name,
                date, muestra,hora,
                identify, codigo
            )
            call.enqueue(object: Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedNorte.isClickable = true
                }

                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    if (response.isSuccessful)
                    {
                        toast("Muestra registrada correctamente")
                        //finish()
                    } else {
                        toast("Ocurrió un error inesperado registrando la muestra")
                        btnConfirmedNorte.isClickable = true

                    }

                }


            })

        }


        else if (btnConfirmedNorte2.isClickable){

            btnConfirmedNorte2.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_norte.text.toString()
            val date = etDate_norte.text.toString()
            val muestra =  spinnerMuestra2_norte.selectedItem.toString()
            val hora = etTime2_norte.text.toString()
            val identify = etID2_norte.text.toString()
            val codigo = etCodigo2_norte.text.toString()



            val call = apiService.storeNorte(
                authHeader, name,
                date, muestra, hora,
                identify, codigo
            )
            call.enqueue(object: Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedNorte2.isClickable = true
                }

                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    if (response.isSuccessful)
                    {
                        toast("Muestra registrada correctamente")
                        //finish()
                    } else {
                        toast("Ocurrió un error inesperado registrando la muestra")
                        btnConfirmedNorte2.isClickable = true

                    }

                }


            })


        }

        else {
            btnConfirmedNorte3.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader = "Bearer $jwt"
            val name = etName_norte.text.toString()
            val date = etDate_norte.text.toString()
            val muestra = spinnerMuestra3_norte.selectedItem.toString()
            val hora = etTime3_norte.text.toString()
            val identify = etID3_norte.text.toString()
            val codigo = etCodigo2_norte.text.toString()



            val call = apiService.storeNorte(
                authHeader, name,
                date, muestra, hora,
                identify, codigo
            )
            call.enqueue(object : Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedNorte3.isClickable = true
                }

                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    if (response.isSuccessful) {
                        toast("Muestra registrada correctamente")
                        //finish()
                    } else {
                        toast("Ocurrió un error inesperado registrando la muestra")
                        btnConfirmedNorte3.isClickable = true

                    }

                }


            })

        }

    }


}