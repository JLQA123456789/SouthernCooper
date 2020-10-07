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
import com.example.southerncooper.model.Molienda
import com.example.southerncooper.util.PreferenceHelper
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.util.toast
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_molienda_c1.*
import kotlinx.android.synthetic.main.activity_molienda_c1_card_view_confirmed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivityMoliendaC1 : AppCompatActivity() {



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
        setContentView(R.layout.activity_molienda_c1)



        photoView = findViewById(R.id.image_view_molienda)


        //button click

        capture_btn_molienda.setOnClickListener{
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

        galery_btn_molienda.setOnClickListener{

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


        btnNext_molienda.setOnClickListener{
            when {
                etName_molienda.text.toString().length<3 -> {

                    etName_molienda.error = getString(R.string.validate_name_description)
                }
                etDate_molienda.text.toString().isEmpty() -> {
                    //isEmpty Indica si se ha inicializado la variable individual

                    etDate_molienda.error = getString(R.string.validate_remolienda_date)
                }
                etTime_molienda.text.toString().isEmpty() -> {

                    etTime_molienda.error = getString(R.string.validate_remolienda_time)
                }
                else -> {

                    Step1_molienda.visibility = View.GONE
                    Step2_molienda.visibility = View.VISIBLE

                }
            }

        }

        btnNext2_molienda.setOnClickListener{
            Step2_molienda.visibility = View.GONE
            Step3_molienda.visibility = View.VISIBLE

        }

        btnRegresar_molienda.setOnClickListener{

            Step1_molienda.visibility = View.VISIBLE
            Step2_molienda.visibility = View.GONE
        }

        btnRegresar2_molienda.setOnClickListener{

            Step2_molienda.visibility = View.VISIBLE
            Step3_molienda.visibility = View.GONE
        }

        btnNext3_molienda.setOnClickListener{

            capture_btn_molienda.visibility = View.GONE
            image_view_molienda.visibility = View.GONE
            galery_btn_molienda.visibility = View.GONE
            showMoliendaDataToConfirm()
            Step3_molienda.visibility = View.GONE
            Step4_molienda.visibility = View.VISIBLE
        }

        btnConfirmedRemo1_molienda.setOnClickListener{

            performStoreMolienda()
        }

        btnConfirmedRemo2_molienda.setOnClickListener{

            performStoreMolienda()
        }

        btnConfirmedRemo3_molienda.setOnClickListener{

            performStoreMolienda()
        }
        btnRegresarMenu_molienda.setOnClickListener{
            finish()
        }


        val MuestraOptions = arrayOf("L2-2A", "L2-2B", "L2-2C")
        spinnerMuestra_molienda.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MuestraOptions)

        val Muestra1Options = arrayOf("L2-2B", "L2-2C", "L2-2A")
        spinnerMuestra2_molienda.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra1Options)

        val Muestra2Options = arrayOf("L2-2C", "L2-2A", "L2-2B")
        spinnerMuestra3_molienda.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra2Options)

        loadIdMolienda()

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
            image_view_molienda.setImageURI(image_uri)
        }


        else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image_view_molienda.setImageURI(data?.data)
        }




    }

    fun onClickDate(v: View?) {

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->

            // Toast.makeText(this,"$y-$m-$d", Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y, m, d)
            etDate_molienda.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    (m+1).twoDigits(),
                    d.twoDigits()
                )
            )
            etDate_molienda.error = null
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
            Step4_molienda.visibility== View.VISIBLE -> {
                Step4_molienda.visibility = View.GONE
                Step3_molienda.visibility = View.VISIBLE
                capture_btn_molienda.visibility = View.VISIBLE
                image_view_molienda.visibility = View.VISIBLE
                galery_btn_molienda.visibility = View.VISIBLE

            }
            Step3_molienda.visibility == View.VISIBLE -> {
                SalirStep()
            }
            Step2_molienda.visibility == View.VISIBLE -> {
                SalirStep()
            }

            Step1_molienda.visibility == View.VISIBLE -> {


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



    private fun showMoliendaDataToConfirm(){

        tvConfirmName_molienda.text =etName_molienda.text.toString()
        tvConfirmDate_molienda.text = etDate_molienda.text.toString()
        tvConfirmMuestra_molienda.text = spinnerMuestra_molienda.selectedItem.toString()
        tvConfirmVertimil_molienda.text = etVertimil_molienda.text.toString()
        tvConfirmTime_molienda.text = etTime_molienda.text.toString()
        tvConfirmID_molienda.text = etID_molienda.selectedItem.toString()
        tvConfirmAVE_molienda.text = etAVE_molienda.text.toString()
        tvConfirmSD_molienda.text = etSD_molienda.text.toString()
        tvConfirmDensity_molienda.text = etDensity_molienda.text.toString()
        tvConfirmSolidoPuro_molienda.text = etSolidoPuro_molienda.text.toString()
        tvConfirmM200_molienda.text = etm200_molienda.text.toString()
        tvConfirmM400_molienda.text =etm400_molienda.text.toString()

        tvConfirmMuestra2_molienda.text = spinnerMuestra2_molienda.selectedItem.toString()
        tvConfirmVertimil2_molienda.text = etVertimil2_molienda.text.toString()
        tvConfirmTime2_molienda.text = etTime2_molienda.text.toString()
        tvConfirmID2_molienda.text = etID2_molienda.selectedItem.toString()
        tvConfirmAVE2_molienda.text = etAVE2_molienda.text.toString()
        tvConfirmSD2_molienda.text = etSD2_molienda.text.toString()
        tvConfirmDensity2_molienda.text = etDensity2_molienda.text.toString()
        tvConfirmSolidoPuro2_molienda.text = etSolidoPuro2_molienda.text.toString()
        tvConfirmM2002_molienda.text = etm2002_molienda.text.toString()
        tvConfirmM4002_molienda.text =etm4002_molienda.text.toString()


        tvConfirmMuestra3_molienda.text = spinnerMuestra3_molienda.selectedItem.toString()
        tvConfirmVertimil3_molienda.text = etVertimil3_molienda.text.toString()
        tvConfirmTime3_molienda.text = etTime3_molienda.text.toString()
        tvConfirmID3_molienda.text = etID3_molienda.selectedItem.toString()
        tvConfirmAVE3_molienda.text = etAVE3_molienda.text.toString()
        tvConfirmSD3_molienda.text = etSD3_molienda.text.toString()
        tvConfirmDensity3_molienda.text = etDensity3_molienda.text.toString()
        tvConfirmSolidoPuro3_molienda.text = etSolidoPuro3_molienda.text.toString()
        tvConfirmM2003_molienda.text = etm2003_molienda.text.toString()
        tvConfirmM4003_molienda.text =etm4003_molienda.text.toString()




    }



    private fun loadIdMolienda(){

        val call = apiService.getMolienda()
        call.enqueue(object : Callback<ArrayList<Molienda>>{
            override fun onFailure(call: Call<ArrayList<Molienda>>, t: Throwable) {
                Toast.makeText(this@ActivityMoliendaC1, getString(R.string.error_loading_ids), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<Molienda>>,
                response: Response<ArrayList<Molienda>>
            ) {
                if (response.isSuccessful) {//{200...300}

                    val molienda = response.body()
                    val moliendaoptions = ArrayList<Int>()
                    molienda?.forEach {
                        moliendaoptions.add(it.identify + 1)
                    }

                    val molienda2 = response.body()
                    val moliendaoptions2 = ArrayList<Int>()
                    molienda?.forEach {
                        moliendaoptions2.add(it.identify + 2)
                    }
                    val molienda3 = response.body()
                    val moliendaoptions3 = ArrayList<Int>()
                    molienda?.forEach {
                        moliendaoptions3.add(it.identify + 3)
                    }



                    etID_molienda.adapter = ArrayAdapter<Int>(this@ActivityMoliendaC1, android.R.layout.simple_list_item_1, moliendaoptions)
                    etID2_molienda.adapter = ArrayAdapter<Int>(this@ActivityMoliendaC1, android.R.layout.simple_list_item_1, moliendaoptions2)
                    etID3_molienda.adapter = ArrayAdapter<Int>(this@ActivityMoliendaC1, android.R.layout.simple_list_item_1, moliendaoptions3)

                }
            }


        })

    }


    private fun performStoreMolienda(){

        if (btnConfirmedRemo1_molienda.isClickable){

            btnConfirmedRemo1_molienda.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_molienda.text.toString()
            val date = etDate_molienda.text.toString()
            val muestra =  spinnerMuestra_molienda.selectedItem.toString()
            val vertimil = etVertimil_molienda.text.toString()
            val hora = etTime_molienda.text.toString()
            val identify = etID_molienda.selectedItem.toString()
            val ave = etAVE_molienda.text.toString()
            val sd = etSD_molienda.text.toString()
            val density = etDensity_molienda.text.toString()
            val solidopuro = etSolidoPuro_molienda.text.toString()
            val m200 = etm200_molienda.text.toString()
            val m400 = etm400_molienda.text.toString()



            val call = apiService.storeMolienda(
                authHeader, name,
                date, muestra, vertimil, hora,
                identify, ave, sd, density,
                solidopuro, m200, m400
            )
            call.enqueue(object: Callback<SimpleResponse>{
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedRemo1_molienda.isClickable = true
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
                        btnConfirmedRemo1_molienda.isClickable = true

                    }

                }


            })

        }


        else if (btnConfirmedRemo2_molienda.isClickable){

            btnConfirmedRemo2_molienda.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_molienda.text.toString()
            val date = etDate_molienda.text.toString()
            val muestra =  spinnerMuestra2_molienda.selectedItem.toString()
            val vertimil = etVertimil2_molienda.text.toString()
            val hora = etTime2_molienda.text.toString()
            val identify = etID2_molienda.selectedItem.toString()
            val ave = etAVE2_molienda.text.toString()
            val sd = etSD2_molienda.text.toString()
            val density = etDensity2_molienda.text.toString()
            val solidopuro = etSolidoPuro2_molienda.text.toString()
            val m200 = etm2002_molienda.text.toString()
            val m400 = etm4002_molienda.text.toString()



            val call = apiService.storeMolienda(
                authHeader, name,
                date, muestra, vertimil, hora,
                identify, ave, sd, density,
                solidopuro, m200, m400
            )
            call.enqueue(object: Callback<SimpleResponse>{
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedRemo1_molienda.isClickable = true
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
                        btnConfirmedRemo1_molienda.isClickable = true

                    }

                }


            })


        }

        else {
            btnConfirmedRemo3_molienda.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader = "Bearer $jwt"
            val name = etName_molienda.text.toString()
            val date = etDate_molienda.text.toString()
            val muestra = spinnerMuestra3_molienda.selectedItem.toString()
            val vertimil = etVertimil3_molienda.text.toString()
            val hora = etTime3_molienda.text.toString()
            val identify = etID3_molienda.selectedItem.toString()
            val ave = etAVE3_molienda.text.toString()
            val sd = etSD3_molienda.text.toString()
            val density = etDensity3_molienda.text.toString()
            val solidopuro = etSolidoPuro3_molienda.text.toString()
            val m200 = etm2003_molienda.text.toString()
            val m400 = etm4003_molienda.text.toString()


            val call = apiService.storeMolienda(
                authHeader, name,
                date, muestra, vertimil, hora,
                identify, ave, sd, density,
                solidopuro, m200, m400
            )
            call.enqueue(object : Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedRemo1_molienda.isClickable = true
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
                        btnConfirmedRemo1_molienda.isClickable = true

                    }

                }


            })

        }

    }


}