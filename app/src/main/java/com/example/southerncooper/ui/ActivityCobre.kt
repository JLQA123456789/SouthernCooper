package com.example.southerncooper.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.io.Response.SimpleResponse
import com.example.southerncooper.util.PreferenceHelper
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.util.toast
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.activity_cobre.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ActivityCobre : AppCompatActivity() {


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
        setContentView(R.layout.activity_cobre)


        val stateProgressBar = findViewById(R.id.progress_cobre) as StateProgressBar
        stateProgressBar.setStateDescriptionData(descriptionData)


        button_cobre.setOnClickListener {


            when (stateProgressBar.getCurrentStateNumber()) {
                1 -> {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    Step1_cobre.visibility = View.GONE
                    Step2_cobre.visibility = View.VISIBLE
                    etName_cobre.error = getString(R.string.validate_name_description)
                    etDate_cobre.error = getString(R.string.validate_remolienda_date)
                    etTime_cobre.error = getString(R.string.validate_remolienda_time)

                }
                2 -> {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                    Step2_cobre.visibility = View.GONE
                    Step3_cobre.visibility = View.VISIBLE
                }

                3 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)

                    Step3_cobre.visibility = View.GONE
                    Step4_cobre.visibility = View.VISIBLE

                }

                else -> { // Note the block

                    if (btnConfirmedCobre.isClickable == true ){

                        toast("Lo siento pero no guardaste")
                        stateProgressBar.setAllStatesCompleted(false)
                        Step4_cobre.visibility = View.VISIBLE
                        Step5_cobre.visibility = View.GONE
                    } else{
                        stateProgressBar.setAllStatesCompleted(true)
                        Step4_cobre.visibility = View.GONE
                        Step5_cobre.visibility = View.VISIBLE
                        button_cobre.visibility = View.GONE
                        button2_cobre.visibility = View.GONE
                    }

                }
            }




        }

        button2_cobre.setOnClickListener {

            when (stateProgressBar.getCurrentStateNumber()) {
                1 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    Step3_cobre.visibility = View.GONE
                    Step2_cobre.visibility = View.VISIBLE
                }

                2 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

                    Step2_cobre.visibility = View.GONE
                    Step1_cobre.visibility = View.VISIBLE
                }
                3 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    Step3_cobre.visibility = View.GONE
                    Step2_cobre.visibility = View.VISIBLE
                }


                else ->  {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)

                    Step4_cobre.visibility = View.GONE
                    Step3_cobre.visibility = View.VISIBLE

                }
            }

        }



        btnConfirmedCobre.setOnClickListener{

            performStoreCobre()
        }

        btnConfirmedCobre2.setOnClickListener{

            performStoreCobre()
        }

        btnConfirmedCobre3.setOnClickListener {

            performStoreCobre()

        }
        btnRegresarCobre.setOnClickListener{
            finish()
        }


        val MuestraOptions = arrayOf(" Reb.Hidro.L1", " Reb.Hidro.L2", "Relave.Final", "Rela.Agot.L1", "Rela.Agot.L2", "Alimt.1ra.Limp", "Conc.1ra.Limp.L1", "Conc.1ra.Limp.L2", "Conc.Colect", "Rel.2da.Limp.L1","Rel.2da.Limp.L2" , "Conc.Prim.L2" , "Conc.Prim.L1")
        spinnerMuestra_cobre.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MuestraOptions)

        val Muestra1Options = arrayOf(" Reb.Hidro.L1", " Reb.Hidro.L2", "Relave.Final", "Rela.Agot.L1", "Rela.Agot.L2", "Alimt.1ra.Limp", "Conc.1ra.Limp.L1", "Conc.1ra.Limp.L2", "Conc.Colect", "Rel.2da.Limp.L1","Rel.2da.Limp.L2" , "Conc.Prim.L2" , "Conc.Prim.L1")
        spinnerMuestra2_cobre.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra1Options)

        val Muestra2Options = arrayOf(" Reb.Hidro.L1", " Reb.Hidro.L2", "Relave.Final", "Rela.Agot.L1", "Rela.Agot.L2", "Alimt.1ra.Limp", "Conc.1ra.Limp.L1", "Conc.1ra.Limp.L2", "Conc.Colect", "Rel.2da.Limp.L1","Rel.2da.Limp.L2" , "Conc.Prim.L2" , "Conc.Prim.L1")
        spinnerMuestra3_cobre.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra2Options)





    }


    fun onClickDate(v: View?) {

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->

            // Toast.makeText(this,"$y-$m-$d", Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y, m, d)
            etDate_cobre.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    (m+1).twoDigits(),
                    d.twoDigits()
                )
            )
            etDate_cobre.error = null
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
            Step4_cobre.visibility== View.VISIBLE -> {
                SalirStep()


            }
            Step3_cobre.visibility == View.VISIBLE -> {
                SalirStep()

            }
            Step2_cobre.visibility == View.VISIBLE -> {
                SalirStep()

            }

            Step1_cobre.visibility == View.VISIBLE -> {
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

    private fun performStoreCobre(){

        if (btnConfirmedCobre.isClickable){

            btnConfirmedCobre.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_cobre.text.toString()
            val date = etDate_cobre.text.toString()
            val muestra =  spinnerMuestra_cobre.selectedItem.toString()
            val hora = etTime_cobre.text.toString()
            val identify = etID_cobre.text.toString()
            val codigo = etCodigo_cobre.text.toString()



            val call = apiService.storeCobre(
                authHeader, name,
                date, muestra,hora,
                identify, codigo
            )
            call.enqueue(object: Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedCobre.isClickable = true
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
                        btnConfirmedCobre.isClickable = true

                    }

                }


            })

        }


        else if (btnConfirmedCobre2.isClickable){

            btnConfirmedCobre2.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_cobre.text.toString()
            val date = etDate_cobre.text.toString()
            val muestra =  spinnerMuestra2_cobre.selectedItem.toString()
            val hora = etTime2_cobre.text.toString()
            val identify = etID2_cobre.text.toString()
            val codigo = etCodigo2_cobre.text.toString()



            val call = apiService.storeCobre(
                authHeader, name,
                date, muestra, hora,
                identify, codigo
            )
            call.enqueue(object: Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedCobre2.isClickable = true
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
                        btnConfirmedCobre2.isClickable = true

                    }

                }


            })


        }

        else {
            btnConfirmedCobre3.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader = "Bearer $jwt"
            val name = etName_cobre.text.toString()
            val date = etDate_cobre.text.toString()
            val muestra = spinnerMuestra3_cobre.selectedItem.toString()
            val hora = etTime3_cobre.text.toString()
            val identify = etID3_cobre.text.toString()
            val codigo = etCodigo2_cobre.text.toString()



            val call = apiService.storeCobre(
                authHeader, name,
                date, muestra, hora,
                identify, codigo
            )
            call.enqueue(object : Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedCobre3.isClickable = true
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
                        btnConfirmedCobre3.isClickable = true

                    }

                }


            })

        }

    }


}