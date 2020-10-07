package com.example.southerncooper.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.southerncooper.R
import com.example.southerncooper.io.ApiService
import com.example.southerncooper.io.Response.SimpleResponse
import com.example.southerncooper.model.RemoliendaC2
import com.example.southerncooper.util.PreferenceHelper
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.util.toast
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_analizadores_id.*
import kotlinx.android.synthetic.main.activity_remolienda_c2.*
import kotlinx.android.synthetic.main.activity_remolienda_c2_card_view_confirmed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivityRemoliendaC2 : AppCompatActivity() {



    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private var photoView:PhotoView?=null

    private val selectedCalendar = Calendar.getInstance()

    //Capture Image Photo
    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    //Storage gallery


    var image_uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remolienda_c2)



        photoView = findViewById(R.id.image_view)


        //button click

        capture_btn_remoliendac2.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    // PERMISO NO ESTABA HABILITADO
                    //PERMISSION WAS  NOT ENABLED
                    val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request  permission
                    // muestra una ventana emergente para solicitar permiso

                    requestPermissions(permission,PERMISSION_CODE)
                } else{

                    //PERMISSION ALREADY GRANTED
                    openCamera()
                }
            } else {

                //SYSTEM  ON IS <MARSHMALLOW
                openCamera()
            }

        }

        galery_btn_remoliendac2.setOnClickListener{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
// muestra una ventana emergente para solicitar permiso de tiempo de ejecución
                    requestPermissions(permissions,
                        PERMISSION_CODE_GALLERY
                    );
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }


        btnNext_remoliendac2.setOnClickListener{
            when {
                etName_remoliendac2.text.toString().length<3 -> {

                    etName_remoliendac2.error = getString(R.string.validate_name_description)
                }
                etDate_remoliendac2.text.toString().isEmpty() -> {
                    //isEmpty Indica si se ha inicializado la variable individual

                    etDate_remoliendac2.error = getString(R.string.validate_remolienda_date)
                }
                etTime_remoliendac2.text.toString().isEmpty() -> {

                    etTime_remoliendac2.error = getString(R.string.validate_remolienda_time)
                }
                else -> {

                    Step1_remoliendac2.visibility = View.GONE
                    Step2_remoliendac2.visibility = View.VISIBLE

                }
            }

        }

        btnNext2_remoliendac2.setOnClickListener{
            Step2_remoliendac2.visibility = View.GONE
            Step3_remoliendac2.visibility = View.VISIBLE

        }

        btnRegresar_remoliendac2.setOnClickListener{

            Step1_remoliendac2.visibility = View.VISIBLE
            Step2_remoliendac2.visibility = View.GONE
        }

        btnRegresar2_remoliendac2.setOnClickListener{

            Step2_remoliendac2.visibility = View.VISIBLE
            Step3_remoliendac2.visibility = View.GONE
        }

        btnNext3_remoliendac2.setOnClickListener{

            capture_btn_remoliendac2.visibility = View.GONE
            image_view_remoliendac2.visibility = View.GONE
            galery_btn_remoliendac2.visibility = View.GONE
            showRemoliendaC2DataToConfirm()
            Step3_remoliendac2.visibility = View.GONE
            Step4_remoliendac2.visibility = View.VISIBLE
        }

        btnConfirmedRemo1_remoliendac2.setOnClickListener{

            performStoreRemoliendaC2()
        }

        btnConfirmedRemo2_remoliendac2.setOnClickListener{

            performStoreRemoliendaC2()
        }

        btnConfirmedRemo3_remoliendac2.setOnClickListener{

            performStoreRemoliendaC2()
        }
        btnRegresarMenu_remoliendac2.setOnClickListener{
            finish()
        }


        val MuestraOptions = arrayOf("L2-2A", "L2-2B", "L2-2C")
        spinnerMuestra_remoliendac2.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MuestraOptions)

        val Muestra1Options = arrayOf("L2-2B", "L2-2C", "L2-2A")
        spinnerMuestra2_remoliendac2.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra1Options)

        val Muestra2Options = arrayOf("L2-2C", "L2-2A", "L2-2B")
        spinnerMuestra3_remoliendac2.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra2Options)

        loadIdRemoliendaC2()

    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"Nueva Imagen")
        values.put(MediaStore.Images.Media.DESCRIPTION,"De la Camara")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE)

    }

    private fun pickImageFromGallery(){
        //Intent to pick image
        //Action_PICK -> Actividad que muestra lista de objetos
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
        //IMAGE_PICK_CODE-> resultcode
    }



    companion object {

        //Permission code
        private const val PERMISSION_CODE_GALLERY = 2000;
        //image pick code
        private const val IMAGE_PICK_CODE = 2002;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //CALLED     WHEN USER PRESSES ALLOW  OR DENY FROM PERMISSION REUQUEST POPUP
        // LLAMADA CUANDO LAS PRENSAS DEL USUARIO PERMITEN O NEGAN DE LA COPIA DE SOLICITUD DE PERMISO
        when(requestCode){
            PERMISSION_CODE->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission from  poppup was granted
                    openCamera()
                }
                else{
                    //persmision from popup was denied
                    Toast.makeText(this, getString(R.string.DeniedPermit), Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSION_CODE_GALLERY ->{

                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, getString(R.string.DeniedPermit), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK  && requestCode == IMAGE_CAPTURE_CODE){
            //set  image captured to image view
            image_view_remoliendac2.setImageURI(image_uri)
        }


        else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image_view_remoliendac2.setImageURI(data?.data)
        }




    }

    fun onClickDate(v: View?) {

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->

            // Toast.makeText(this,"$y-$m-$d", Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y, m, d)
            etDate_remoliendac2.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    (m+1).twoDigits(),
                    d.twoDigits()
                )
            )
            etDate_remoliendac2.error = null
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
            Step4_remoliendac2.visibility== View.VISIBLE -> {
                Step4_remoliendac2.visibility = View.GONE
                Step3_remoliendac2.visibility = View.VISIBLE
                capture_btn_remoliendac2.visibility = View.VISIBLE
                image_view_remoliendac2.visibility = View.VISIBLE
                galery_btn_remoliendac2.visibility = View.VISIBLE

            }
            Step3_remoliendac2.visibility == View.VISIBLE -> {
                Step2_remoliendac2.visibility = View.VISIBLE
                capture_btn_remoliendac2.visibility = View.VISIBLE
                galery_btn_remoliendac2.visibility = View.VISIBLE
                image_view_remoliendac2.visibility = View.VISIBLE
                Step3_remoliendac2.visibility = View.GONE
            }
            Step2_remoliendac2.visibility == View.VISIBLE -> {
                Step1_remoliendac2.visibility = View.VISIBLE
                Step2_remoliendac2.visibility = View.GONE
                capture_btn_remoliendac2.visibility = View.VISIBLE
                galery_btn_remoliendac2.visibility = View.VISIBLE
                image_view_remoliendac2.visibility = View.VISIBLE
            }

            Step1_remoliendac2.visibility == View.VISIBLE -> {


                capture_btn_remoliendac2.visibility = View.VISIBLE
                galery_btn_remoliendac2.visibility = View.VISIBLE
                image_view_remoliendac2.visibility = View.VISIBLE

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
        }
    }




    private fun showRemoliendaC2DataToConfirm(){

        tvConfirmName_remoliendac2.text =etName_remoliendac2.text.toString()
        tvConfirmDate_remoliendac2.text = etDate_remoliendac2.text.toString()
        tvConfirmMuestra_remoliendac2.text = spinnerMuestra_remoliendac2.selectedItem.toString()
        tvConfirmVertimil_remoliendac2.text = etVertimil_remoliendac2.text.toString()
        tvConfirmTime_remoliendac2.text = etTime_remoliendac2.text.toString()
        tvConfirmID_remoliendac2.text = etID_remoliendac2.selectedItem.toString()
        tvConfirmAVE_remoliendac2.text = etAVE_remoliendac2.text.toString()
        tvConfirmSD_remoliendac2.text = etSD_remoliendac2.text.toString()
        tvConfirmDensity_remoliendac2.text = etDensity_remoliendac2.text.toString()
        tvConfirmSolidoPuro_remoliendac2.text = etSolidoPuro_remoliendac2.text.toString()
        tvConfirmM200_remoliendac2.text = etm200_remoliendac2.text.toString()
        tvConfirmM400_remoliendac2.text =etm400_remoliendac2.text.toString()

        tvConfirmMuestra2_remoliendac2.text = spinnerMuestra2_remoliendac2.selectedItem.toString()
        tvConfirmVertimil2_remoliendac2.text = etVertimil2_remoliendac2.text.toString()
        tvConfirmTime2_remoliendac2.text = etTime2_remoliendac2.text.toString()
        tvConfirmID2_remoliendac2.text = etID2_remoliendac2.selectedItem.toString()
        tvConfirmAVE2_remoliendac2.text = etAVE2_remoliendac2.text.toString()
        tvConfirmSD2_remoliendac2.text = etSD2_remoliendac2.text.toString()
        tvConfirmDensity2_remoliendac2.text = etDensity2_remoliendac2.text.toString()
        tvConfirmSolidoPuro2_remoliendac2.text = etSolidoPuro2_remoliendac2.text.toString()
        tvConfirmM2002_remoliendac2.text = etm2002_remoliendac2.text.toString()
        tvConfirmM4002_remoliendac2.text =etm4002_remoliendac2.text.toString()


        tvConfirmMuestra3_remoliendac2.text = spinnerMuestra3_remoliendac2.selectedItem.toString()
        tvConfirmVertimil3_remoliendac2.text = etVertimil3_remoliendac2.text.toString()
        tvConfirmTime3_remoliendac2.text = etTime3_remoliendac2.text.toString()
        tvConfirmID3_remoliendac2.text = etID3_remoliendac2.selectedItem.toString()
        tvConfirmAVE3_remoliendac2.text = etAVE3_remoliendac2.text.toString()
        tvConfirmSD3_remoliendac2.text = etSD3_remoliendac2.text.toString()
        tvConfirmDensity3_remoliendac2.text = etDensity3_remoliendac2.text.toString()
        tvConfirmSolidoPuro3_remoliendac2.text = etSolidoPuro3_remoliendac2.text.toString()
        tvConfirmM2003_remoliendac2.text = etm2003_remoliendac2.text.toString()
        tvConfirmM4003_remoliendac2.text =etm4003_remoliendac2.text.toString()




    }



    private fun loadIdRemoliendaC2(){

        val call = apiService.getRemoliendaC2()
        call.enqueue(object : Callback<ArrayList<RemoliendaC2>>{
            override fun onFailure(call: Call<ArrayList<RemoliendaC2>>, t: Throwable) {
                Toast.makeText(this@ActivityRemoliendaC2, getString(R.string.error_loading_ids), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<RemoliendaC2>>,
                response: Response<ArrayList<RemoliendaC2>>
            ) {
                if (response.isSuccessful) {//{200...300}

                    val remolienda = response.body()
                    val remoliendaoptions = ArrayList<Int>()
                    remolienda?.forEach {
                        remoliendaoptions.add(it.identify + 1)
                    }

                    val remolienda2 = response.body()
                    val remoliendaoptions2 = ArrayList<Int>()
                    remolienda2?.forEach {
                        remoliendaoptions2.add(it.identify + 2)
                    }
                    val remolienda3 = response.body()
                    val remoliendaoptions3 = ArrayList<Int>()
                    remolienda3?.forEach {
                        remoliendaoptions3.add(it.identify + 3)
                    }



                    etID_remoliendac2.adapter = ArrayAdapter<Int>(this@ActivityRemoliendaC2, android.R.layout.simple_list_item_1, remoliendaoptions)
                    etID2_remoliendac2.adapter = ArrayAdapter<Int>(this@ActivityRemoliendaC2, android.R.layout.simple_list_item_1, remoliendaoptions2)
                    etID3_remoliendac2.adapter = ArrayAdapter<Int>(this@ActivityRemoliendaC2, android.R.layout.simple_list_item_1, remoliendaoptions3)

                }
            }


        })

    }


    private fun performStoreRemoliendaC2(){

        if (btnConfirmedRemo1_remoliendac2.isClickable){

            btnConfirmedRemo1_remoliendac2.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_remoliendac2.text.toString()
            val date = etDate_remoliendac2.text.toString()
            val muestra =  spinnerMuestra_remoliendac2.selectedItem.toString()
            val vertimil = etVertimil_remoliendac2.text.toString()
            val hora = etTime_remoliendac2.text.toString()
            val identify = etID_remoliendac2.selectedItem.toString()
            val ave = etAVE_remoliendac2.text.toString()
            val sd = etSD_remoliendac2.text.toString()
            val density = etDensity_remoliendac2.text.toString()
            val solidopuro = etSolidoPuro_remoliendac2.text.toString()
            val m200 = etm200_remoliendac2.text.toString()
            val m400 = etm400_remoliendac2.text.toString()



            val call = apiService.storeRemoliendaC2(
                authHeader, name,
                date, muestra, vertimil, hora,
                identify, ave, sd, density,
                solidopuro, m200, m400
            )
            call.enqueue(object: Callback<SimpleResponse>{
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedRemo1_remoliendac2.isClickable = true
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
                        btnConfirmedRemo1_remoliendac2.isClickable = true

                    }

                }


            })

        }


        else if (btnConfirmedRemo2_remoliendac2.isClickable){

            btnConfirmedRemo2_remoliendac2.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_remoliendac2.text.toString()
            val date = etDate_remoliendac2.text.toString()
            val muestra =  spinnerMuestra2_remoliendac2.selectedItem.toString()
            val vertimil = etVertimil2_remoliendac2.text.toString()
            val hora = etTime2_remoliendac2.text.toString()
            val identify = etID2_remoliendac2.selectedItem.toString()
            val ave = etAVE2_remoliendac2.text.toString()
            val sd = etSD2_remoliendac2.text.toString()
            val density = etDensity2_remoliendac2.text.toString()
            val solidopuro = etSolidoPuro2_remoliendac2.text.toString()
            val m200 = etm2002_remoliendac2.text.toString()
            val m400 = etm4002_remoliendac2.text.toString()



            val call = apiService.storeRemoliendaC2(
                authHeader, name,
                date, muestra, vertimil, hora,
                identify, ave, sd, density,
                solidopuro, m200, m400
            )
            call.enqueue(object: Callback<SimpleResponse>{
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedRemo1_remoliendac2.isClickable = true
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
                        btnConfirmedRemo1_remoliendac2.isClickable = true

                    }

                }


            })


        }

        else {
            btnConfirmedRemo3_remoliendac2.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader = "Bearer $jwt"
            val name = etName_remoliendac2.text.toString()
            val date = etDate_remoliendac2.text.toString()
            val muestra = spinnerMuestra3_remoliendac2.selectedItem.toString()
            val vertimil = etVertimil3_remoliendac2.text.toString()
            val hora = etTime3_remoliendac2.text.toString()
            val identify = etID3_remoliendac2.selectedItem.toString()
            val ave = etAVE3_remoliendac2.text.toString()
            val sd = etSD3_remoliendac2.text.toString()
            val density = etDensity3_remoliendac2.text.toString()
            val solidopuro = etSolidoPuro3_remoliendac2.text.toString()
            val m200 = etm2003_remoliendac2.text.toString()
            val m400 = etm4003_remoliendac2.text.toString()


            val call = apiService.storeRemoliendaC2(
                authHeader, name,
                date, muestra, vertimil, hora,
                identify, ave, sd, density,
                solidopuro, m200, m400
            )
            call.enqueue(object : Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedRemo1_remoliendac2.isClickable = true
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
                        btnConfirmedRemo1_remoliendac2.isClickable = true

                    }

                }


            })

        }

    }


}