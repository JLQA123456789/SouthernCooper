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
import kotlinx.android.synthetic.main.activity_moly.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ActivityMoly : AppCompatActivity() {


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
        setContentView(R.layout.activity_moly)


        val stateProgressBar = findViewById(R.id.progress_moly) as StateProgressBar
        stateProgressBar.setStateDescriptionData(descriptionData)


        button_moly.setOnClickListener {


            when (stateProgressBar.getCurrentStateNumber()) {
                1 -> {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    Step1_moly.visibility = View.GONE
                    Step2_moly.visibility = View.VISIBLE
                    etName_moly.error = getString(R.string.validate_name_description)
                    etDate_moly.error = getString(R.string.validate_remolienda_date)
                    etTime_moly.error = getString(R.string.validate_remolienda_time)

                }
                2 -> {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                    Step2_moly.visibility = View.GONE
                    Step3_moly.visibility = View.VISIBLE
                }

                3 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)

                    Step3_moly.visibility = View.GONE
                    Step4_moly.visibility = View.VISIBLE

                }

                else -> { // Note the block

                    if (btnConfirmedMoly.isClickable == true ){

                        toast("Lo siento pero no guardaste")
                        stateProgressBar.setAllStatesCompleted(false)
                        Step4_moly.visibility = View.VISIBLE
                        Step5_moly.visibility = View.GONE
                    } else{
                        stateProgressBar.setAllStatesCompleted(true)
                        Step4_moly.visibility = View.GONE
                        Step5_moly.visibility = View.VISIBLE
                        button_moly.visibility = View.GONE
                        button2_moly.visibility = View.GONE
                    }

                }
            }




        }

        button2_moly.setOnClickListener {

            when (stateProgressBar.getCurrentStateNumber()) {
                1 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    Step3_moly.visibility = View.GONE
                    Step2_moly.visibility = View.VISIBLE
                }

                2 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

                    Step2_moly.visibility = View.GONE
                    Step1_moly.visibility = View.VISIBLE
                }
                3 -> {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    Step3_moly.visibility = View.GONE
                    Step2_moly.visibility = View.VISIBLE
                }


                else ->  {stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)

                    Step4_moly.visibility = View.GONE
                    Step3_moly.visibility = View.VISIBLE

                }
            }

        }


        btnConfirmedMoly.setOnClickListener{

            performStoreMoly()
        }

        btnConfirmedMoly2.setOnClickListener{

            performStoreMoly()
        }

        btnConfirmedMoly3.setOnClickListener {

            performStoreMoly()

        }
        btnRegresarMoly.setOnClickListener{
            finish()
        }


        val MuestraOptions = arrayOf(" Alim Rougher", " Alim Rougher 1", "Alim Rougher 2", "Alim Rougher 3", "Alim 1er Cleaner", "Conc 7ma Limpieza", "Conc 8va Limpieza", "Conc Ult. Limpieza")
        spinnerMuestra_moly.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MuestraOptions)

        val Muestra1Options = arrayOf(" Alim Rougher", " Alim Rougher 1", "Alim Rougher 2", "Alim Rougher 3", "Alim 1er Cleaner", "Conc 7ma Limpieza", "Conc 8va Limpieza", "Conc Ult. Limpieza")
        spinnerMuestra2_moly.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra1Options)

        val Muestra2Options = arrayOf(" Alim Rougher", " Alim Rougher 1", "Alim Rougher 2", "Alim Rougher 3", "Alim 1er Cleaner", "Conc 7ma Limpieza", "Conc 8va Limpieza", "Conc Ult. Limpieza")
        spinnerMuestra3_moly.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra2Options)





    }


    fun onClickDate(v: View?) {

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->

            // Toast.makeText(this,"$y-$m-$d", Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y, m, d)
            etDate_moly.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    (m+1).twoDigits(),
                    d.twoDigits()
                )
            )
            etDate_moly.error = null
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

            Step4_moly.visibility== View.VISIBLE -> {
                SalirStep()


            }
            Step3_moly.visibility == View.VISIBLE -> {
                SalirStep()

            }
            Step2_moly.visibility == View.VISIBLE -> {
                SalirStep()

            }

            Step1_moly.visibility == View.VISIBLE -> {
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

    private fun performStoreMoly(){

        if (btnConfirmedMoly.isClickable){

            btnConfirmedMoly.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_moly.text.toString()
            val date = etDate_moly.text.toString()
            val muestra =  spinnerMuestra_moly.selectedItem.toString()
            val hora = etTime_moly.text.toString()
            val identify = etID_moly.text.toString()
            val codigo = etCodigo_moly.text.toString()

            val call = apiService.storeMoly(
                authHeader, name,
                date, muestra,hora,
                identify, codigo
            )
            call.enqueue(object: Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedMoly.isClickable = true
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
                        btnConfirmedMoly.isClickable = true

                    }

                }


            })

        }


        else if (btnConfirmedMoly2.isClickable){

            btnConfirmedMoly2.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_moly.text.toString()
            val date = etDate_moly.text.toString()
            val muestra =  spinnerMuestra2_moly.selectedItem.toString()
            val hora = etTime2_moly.text.toString()
            val identify = etID2_moly.text.toString()
            val codigo = etCodigo2_moly.text.toString()




            val call = apiService.storeMoly(
                authHeader, name,
                date, muestra, hora,
                identify, codigo
            )
            call.enqueue(object: Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedMoly2.isClickable = true
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
                        btnConfirmedMoly2.isClickable = true

                    }

                }


            })


        }

        else {
            btnConfirmedMoly3.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader = "Bearer $jwt"
            val name = etName_moly.text.toString()
            val date = etDate_moly.text.toString()
            val muestra = spinnerMuestra3_moly.selectedItem.toString()
            val hora = etTime3_moly.text.toString()
            val identify = etID3_moly.text.toString()
            val codigo = etCodigo2_moly.text.toString()



            val call = apiService.storeMoly(
                authHeader, name,
                date, muestra, hora,
                identify, codigo
            )
            call.enqueue(object : Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedMoly3.isClickable = true
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
                        btnConfirmedMoly3.isClickable = true

                    }

                }


            })

        }

    }


}