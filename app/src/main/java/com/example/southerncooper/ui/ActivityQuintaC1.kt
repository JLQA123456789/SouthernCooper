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
import com.example.southerncooper.model.Quinta
import com.example.southerncooper.util.PreferenceHelper
import com.example.southerncooper.util.PreferenceHelper.get
import com.example.southerncooper.util.toast
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_analizadores_id.*
import kotlinx.android.synthetic.main.activity_quinta_c1.*
import kotlinx.android.synthetic.main.activity_quinta_c1_card_view_confirmed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivityQuintaC1 : AppCompatActivity() {


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
        setContentView(R.layout.activity_quinta_c1)



        photoView = findViewById(R.id.image_view)


        //button click

        capture_btn_quinta.setOnClickListener{
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

        galery_btn_quinta.setOnClickListener{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
// muestra una ventana emergente para solicitar permiso de tiempo de ejecución
                    requestPermissions(permissions,
                        ActivityQuintaC1.PERMISSION_CODE_GALLERY
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


        btnNext_quinta.setOnClickListener{
            when {
                etName_quinta.text.toString().length<3 -> {

                    etName_quinta.error = getString(R.string.validate_name_description)
                }
                etDate_quinta.text.toString().isEmpty() -> {
                    //isEmpty Indica si se ha inicializado la variable individual

                    etDate_quinta.error = getString(R.string.validate_remolienda_date)
                }
                etTime_quinta.text.toString().isEmpty() -> {

                    etTime_quinta.error = getString(R.string.validate_remolienda_time)
                }
                else -> {

                    Step1_quinta.visibility = View.GONE
                    Step2_quinta.visibility = View.VISIBLE

                }
            }

        }

        btnNext2_quinta.setOnClickListener{
            Step2_quinta.visibility = View.GONE
            Step3_quinta.visibility = View.VISIBLE

        }

        btnRegresar_quinta.setOnClickListener{

            Step1_quinta.visibility = View.VISIBLE
            Step2_quinta.visibility = View.GONE
        }

        btnRegresar2_quinta.setOnClickListener{

            Step2_quinta.visibility = View.VISIBLE
            Step3_quinta.visibility = View.GONE
        }

        btnNext3_quinta.setOnClickListener{

            capture_btn_quinta.visibility = View.GONE
            image_view_quinta.visibility = View.GONE
            galery_btn_quinta.visibility = View.GONE
            showQuintaDataToConfirm()
            Step3_quinta.visibility = View.GONE
            Step4_quinta.visibility = View.VISIBLE
        }

        btnConfirmedRemo1_quinta.setOnClickListener{

            performStoreQuinta()
        }

        btnConfirmedRemo2_quinta.setOnClickListener{

            performStoreQuinta()
        }

        btnConfirmedRemo3_quinta.setOnClickListener{

            performStoreQuinta()
        }
        btnRegresarMenu_quinta.setOnClickListener{
            finish()
        }


        val MuestraOptions = arrayOf("L2-2A", "L2-2B", "L2-2C")
        spinnerMuestra_quinta.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MuestraOptions)

        val Muestra1Options = arrayOf("L2-2B", "L2-2C", "L2-2A")
        spinnerMuestra2_quinta.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra1Options)

        val Muestra2Options = arrayOf("L2-2C", "L2-2A", "L2-2B")
        spinnerMuestra3_quinta.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra2Options)

        loadIdQuinta()

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
            image_view_quinta.setImageURI(image_uri)
        }


        else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image_view_quinta.setImageURI(data?.data)
        }




    }

    fun onClickDate(v: View?) {

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->

            // Toast.makeText(this,"$y-$m-$d", Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y, m, d)
            etDate_quinta.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    (m+1).twoDigits(),
                    d.twoDigits()
                )
            )
            etDate_quinta.error = null
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
            Step4_quinta.visibility== View.VISIBLE -> {
                Step4_quinta.visibility = View.GONE
                Step3_quinta.visibility = View.VISIBLE
                capture_btn_quinta.visibility = View.VISIBLE
                image_view_quinta.visibility = View.VISIBLE
                galery_btn_quinta.visibility = View.VISIBLE

            }
            Step3_quinta.visibility == View.VISIBLE -> {
                Step2_quinta.visibility = View.VISIBLE
                capture_btn_quinta.visibility = View.VISIBLE
                galery_btn_quinta.visibility = View.VISIBLE
                image_view_quinta.visibility = View.VISIBLE
                Step3_quinta.visibility = View.GONE
            }
            Step2_quinta.visibility == View.VISIBLE -> {
                Step1_quinta.visibility = View.VISIBLE
                Step2_quinta.visibility = View.GONE
                capture_btn_quinta.visibility = View.VISIBLE
                galery_btn_quinta.visibility = View.VISIBLE
                image_view_quinta.visibility = View.VISIBLE
            }

            Step1_quinta.visibility == View.VISIBLE -> {


                capture_btn_quinta.visibility = View.VISIBLE
                galery_btn_quinta.visibility = View.VISIBLE
                image_view_quinta.visibility = View.VISIBLE

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




    private fun showQuintaDataToConfirm(){

        tvConfirmName_quinta.text =etName_quinta.text.toString()
        tvConfirmDate_quinta.text = etDate_quinta.text.toString()
        tvConfirmMuestra_quinta.text = spinnerMuestra_quinta.selectedItem.toString()
        tvConfirmTime_quinta.text = etTime_quinta.text.toString()
        tvConfirmID_quinta.text = etID_quinta.selectedItem.toString()
        tvConfirmAVE_quinta.text = etAVE_quinta.text.toString()
        tvConfirmSD_quinta.text = etSD_quinta.text.toString()
        tvConfirmM48_quinta.text = etm48_quinta.text.toString()
        tvConfirmM65_quinta.text =etm65_quinta.text.toString()

        tvConfirmMuestra2_quinta.text = spinnerMuestra2_quinta.selectedItem.toString()
        tvConfirmTime2_quinta.text = etTime2_quinta.text.toString()
        tvConfirmID2_quinta.text = etID2_quinta.selectedItem.toString()
        tvConfirmAVE2_quinta.text = etAVE2_quinta.text.toString()
        tvConfirmSD2_quinta.text = etSD2_quinta.text.toString()
        tvConfirmM482_quinta.text = etm482_quinta.text.toString()
        tvConfirmM652_quinta.text =etm652_quinta.text.toString()


        tvConfirmMuestra3_quinta.text = spinnerMuestra3_quinta.selectedItem.toString()
        tvConfirmTime3_quinta.text = etTime3_quinta.text.toString()
        tvConfirmID3_quinta.text = etID3_quinta.selectedItem.toString()
        tvConfirmAVE3_quinta.text = etAVE3_quinta.text.toString()
        tvConfirmSD3_quinta.text = etSD3_quinta.text.toString()
        tvConfirmM483_quinta.text = etm483_quinta.text.toString()
        tvConfirmM653_quinta.text =etm653_quinta.text.toString()




    }



    private fun loadIdQuinta(){

        val call = apiService.getQuinta()
        call.enqueue(object : Callback<ArrayList<Quinta>>{
            override fun onFailure(call: Call<ArrayList<Quinta>>, t: Throwable) {
                Toast.makeText(this@ActivityQuintaC1, getString(R.string.error_loading_ids), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<Quinta>>,
                response: Response<ArrayList<Quinta>>
            ) {
                if (response.isSuccessful) {//{200...300}

                    val quinta = response.body()
                    val quintaoptions = ArrayList<Int>()
                    quinta?.forEach {
                        quintaoptions.add(it.identify + 1)
                    }

                    val quinta2 = response.body()
                    val quintaoptions2 = ArrayList<Int>()
                    quinta2?.forEach {
                        quintaoptions2.add(it.identify + 2)
                    }
                    val quinta3 = response.body()
                    val quintaoptions3 = ArrayList<Int>()
                    quinta3?.forEach {
                        quintaoptions3.add(it.identify + 3)
                    }



                    etID_quinta.adapter = ArrayAdapter<Int>(this@ActivityQuintaC1, android.R.layout.simple_list_item_1, quintaoptions)
                    etID2_quinta.adapter = ArrayAdapter<Int>(this@ActivityQuintaC1, android.R.layout.simple_list_item_1, quintaoptions2)
                    etID3_quinta.adapter = ArrayAdapter<Int>(this@ActivityQuintaC1, android.R.layout.simple_list_item_1, quintaoptions3)

                }
            }


        })

    }


    private fun performStoreQuinta(){

        if (btnConfirmedRemo1_quinta.isClickable){

            btnConfirmedRemo1_quinta.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_quinta.text.toString()
            val date = etDate_quinta.text.toString()
            val muestra =  spinnerMuestra_quinta.selectedItem.toString()
            val hora = etTime_quinta.text.toString()
            val identify = etID_quinta.selectedItem.toString()
            val ave = etAVE_quinta.text.toString()
            val sd = etSD_quinta.text.toString()
            val m48 = etm48_quinta.text.toString()
            val m65 = etm65_quinta.text.toString()



            val call = apiService.storeQuinta(
                authHeader, name,
                date, muestra,  hora,
                identify, ave, sd, m48, m65
            )
            call.enqueue(object: Callback<SimpleResponse>{
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedRemo1_quinta.isClickable = true
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
                        btnConfirmedRemo1_quinta.isClickable = true

                    }

                }


            })

        }


        else if (btnConfirmedRemo2_quinta.isClickable){

            btnConfirmedRemo2_quinta.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader =  "Bearer $jwt"
            val name = etName_quinta.text.toString()
            val date = etDate_quinta.text.toString()
            val muestra =  spinnerMuestra2_quinta.selectedItem.toString()
            val hora = etTime2_quinta.text.toString()
            val identify = etID2_quinta.selectedItem.toString()
            val ave = etAVE2_quinta.text.toString()
            val sd = etSD2_quinta.text.toString()
            val m48 = etm482_quinta.text.toString()
            val m65 = etm652_quinta.text.toString()



            val call = apiService.storeQuinta(
                authHeader, name,
                date, muestra, hora,
                identify, ave, sd, m48, m65
            )
            call.enqueue(object: Callback<SimpleResponse>{
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedRemo1_quinta.isClickable = true
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
                        btnConfirmedRemo1_quinta.isClickable = true

                    }

                }


            })


        }

        else {
            btnConfirmedRemo3_quinta.isClickable = false

            val jwt = preferences["jwt", ""]
            val authHeader = "Bearer $jwt"
            val name = etName_quinta.text.toString()
            val date = etDate_quinta.text.toString()
            val muestra = spinnerMuestra3_quinta.selectedItem.toString()
            val hora = etTime3_quinta.text.toString()
            val identify = etID3_quinta.selectedItem.toString()
            val ave = etAVE3_quinta.text.toString()
            val sd = etSD3_quinta.text.toString()
            val m48 = etm483_quinta.text.toString()
            val m65 = etm653_quinta.text.toString()


            val call = apiService.storeQuinta(
                authHeader, name,
                date, muestra, hora,
                identify, ave, sd, m48, m65
            )
            call.enqueue(object : Callback<SimpleResponse> {
                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                    btnConfirmedRemo1_quinta.isClickable = true
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
                        btnConfirmedRemo1_quinta.isClickable = true

                    }

                }


            })

        }

    }

}