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
import com.example.southerncooper.model.Remolienda
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_analizadores_id.*
import kotlinx.android.synthetic.main.activity_remolienda_c1.*
import kotlinx.android.synthetic.main.activity_remolienda_c1_card_view_confirmed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivityRemoliendaC1 : AppCompatActivity() {


    private val apiService: ApiService by lazy{
        ApiService.create()
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
        setContentView(R.layout.activity_remolienda_c1)



        photoView = findViewById(R.id.image_view)


        //button click

        capture_btn.setOnClickListener{
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

        galery_btn.setOnClickListener{

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


        btnNext.setOnClickListener{
            when {
                etName.text.toString().length<3 -> {

                    etName.error = getString(R.string.validate_name_description)
                }
                etDate.text.toString().isEmpty() -> {
                    //isEmpty Indica si se ha inicializado la variable individual

                    etDate.error = getString(R.string.validate_remolienda_date)
                }
                etTime.text.toString().isEmpty() -> {

                    etTime.error = getString(R.string.validate_remolienda_time)
                }
                else -> {

                    Step1.visibility = View.GONE
                    Step2.visibility = View.VISIBLE

                }
            }

        }

        btnNext2.setOnClickListener{
            Step2.visibility = View.GONE
            Step3.visibility = View.VISIBLE

        }

        btnRegresar.setOnClickListener{

            Step1.visibility = View.VISIBLE
            Step2.visibility = View.GONE
        }

        btnRegresar2.setOnClickListener{

            Step2.visibility = View.VISIBLE
            Step3.visibility = View.GONE
        }

        btnNext3.setOnClickListener{

            capture_btn.visibility = View.GONE
            image_view.visibility = View.GONE
            galery_btn.visibility = View.GONE
            showRemoliendaDataToConfirm()
            Step3.visibility = View.GONE
            Step4.visibility = View.VISIBLE
        }

        btnConfirmedRemo1.setOnClickListener{
            Toast.makeText(this, "La muestra se ha registrado correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }

//        btnConfirmedRemo1.setOnClickListener{
//            Toast.makeText(this, "La muestra se ha registrado correctamente", Toast.LENGTH_SHORT).show()
//            finish()
//        }


        val MuestraOptions = arrayOf("L2-2A", "L2-2B", "L2-2C")
        spinnerMuestra.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MuestraOptions)

        val Muestra1Options = arrayOf("L2-2B", "L2-2C", "L2-2A")
        spinnerMuestra2.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra1Options)

        val Muestra2Options = arrayOf("L2-2C", "L2-2A", "L2-2B")
        spinnerMuestra3.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Muestra2Options)

        loadIdRemolienda()

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
                    image_view.setImageURI(image_uri)
                }


               else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
                    image_view.setImageURI(data?.data)
                }




    }

    fun onClickDate(v: View?) {

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->

            // Toast.makeText(this,"$y-$m-$d", Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y, m, d)
            etDate.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    (m+1).twoDigits(),
                    d.twoDigits()
                )
            )
                etDate.error = null
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
        Step4.visibility== View.VISIBLE -> {
            Step4.visibility = View.GONE
            Step3.visibility = View.VISIBLE
            capture_btn.visibility = View.VISIBLE
            image_view.visibility = View.VISIBLE
            galery_btn.visibility = View.VISIBLE

        }
        Step3.visibility == View.VISIBLE -> {
            Step2.visibility = View.VISIBLE
            capture_btn.visibility = View.VISIBLE
            galery_btn.visibility = View.VISIBLE
            image_view.visibility = View.VISIBLE
            Step3.visibility = View.GONE
        }
        Step2.visibility == View.VISIBLE -> {
            Step1.visibility = View.VISIBLE
            Step2.visibility = View.GONE
            capture_btn.visibility = View.VISIBLE
            galery_btn.visibility = View.VISIBLE
            image_view.visibility = View.VISIBLE
        }

        Step1.visibility == View.VISIBLE -> {


            capture_btn.visibility = View.VISIBLE
            galery_btn.visibility = View.VISIBLE
            image_view.visibility = View.VISIBLE

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




    private fun showRemoliendaDataToConfirm(){

        tvConfirmName.text =etName.text.toString()
        tvConfirmDate.text = etDate.text.toString()
        tvConfirmMuestra.text = spinnerMuestra.selectedItem.toString()
        tvConfirmVertimil.text = etVertimil.text.toString()
        tvConfirmTime.text = etTime.text.toString()
        tvConfirmID.text = etID.selectedItem.toString()
        tvConfirmAVE.text = etAVE.text.toString()
        tvConfirmSD.text = etSD.text.toString()
        tvConfirmDensity.text = etDensity.text.toString()
        tvConfirmSolidoPuro.text = etSolidoPuro.text.toString()
        tvConfirmM200.text = etm200.text.toString()
        tvConfirmM400.text =etm400.text.toString()


        tvConfirmMuestra2.text = spinnerMuestra2.selectedItem.toString()
        tvConfirmVertimil2.text = etVertimil2.text.toString()
        tvConfirmTime2.text = etTime2.text.toString()
        tvConfirmID2.text = etID2.selectedItem.toString()
        tvConfirmAVE2.text = etAVE2.text.toString()
        tvConfirmSD2.text = etSD2.text.toString()
        tvConfirmDensity2.text = etDensity2.text.toString()
        tvConfirmSolidoPuro2.text = etSolidoPuro2.text.toString()
        tvConfirmM2002.text = etm2002.text.toString()
        tvConfirmM4002.text =etm4002.text.toString()


        tvConfirmMuestra3.text = spinnerMuestra3.selectedItem.toString()
        tvConfirmVertimil3.text = etVertimil3.text.toString()
        tvConfirmTime3.text = etTime3.text.toString()
        tvConfirmID3.text = etID3.selectedItem.toString()
        tvConfirmAVE3.text = etAVE3.text.toString()
        tvConfirmSD3.text = etSD3.text.toString()
        tvConfirmDensity3.text = etDensity3.text.toString()
        tvConfirmSolidoPuro3.text = etSolidoPuro3.text.toString()
        tvConfirmM2003.text = etm2003.text.toString()
        tvConfirmM4003.text =etm4003.text.toString()


    }



    private fun loadIdRemolienda(){

        val call = apiService.getRemolienda()
        call.enqueue(object : Callback<ArrayList<Remolienda>>{
            override fun onFailure(call: Call<ArrayList<Remolienda>>, t: Throwable) {
                Toast.makeText(this@ActivityRemoliendaC1, getString(R.string.error_loading_ids), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<Remolienda>>,
                response: Response<ArrayList<Remolienda>>
            ) {
                if (response.isSuccessful) {//{200...300}

                    val remolienda = response.body()
                    val remoliendaoptions = ArrayList<Int>()
                    remolienda?.forEach {
                        remoliendaoptions.add(it.identify + 1)
                    }

                    val remolienda2 = response.body()
                    val remoliendaoptions2 = ArrayList<Int>()
                    remolienda?.forEach {
                        remoliendaoptions2.add(it.identify + 2)
                    }
                    val remolienda3 = response.body()
                    val remoliendaoptions3 = ArrayList<Int>()
                    remolienda?.forEach {
                        remoliendaoptions3.add(it.identify + 3)
                    }



                    etID.adapter = ArrayAdapter<Int>(this@ActivityRemoliendaC1, android.R.layout.simple_list_item_1, remoliendaoptions)
                    etID2.adapter = ArrayAdapter<Int>(this@ActivityRemoliendaC1, android.R.layout.simple_list_item_1, remoliendaoptions2)
                    etID3.adapter = ArrayAdapter<Int>(this@ActivityRemoliendaC1, android.R.layout.simple_list_item_1, remoliendaoptions3)

                }
            }


        })

    }

}