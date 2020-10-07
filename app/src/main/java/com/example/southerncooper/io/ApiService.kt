package com.example.southerncooper.io

import com.example.southerncooper.io.Response.LoginResponse
import com.example.southerncooper.io.Response.SimpleResponse
import com.example.southerncooper.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.sql.Date
import java.sql.Time

interface ApiService {

    @GET("user")
    @Headers("Accept: application/json")
    fun getUser(@Header("Authorization") authHeader: String):Call<User>


    @POST("user")
    @Headers("Accept: application/json")
    fun postUser(@Header("Authorization") authHeader: String,
                 @Query("name")name:String,
                 @Query("phone")phone:String,
                 @Query("area")area:String):Call<Void>





    @POST("login")
    //Pasamos un parametro sobre la consulta eso se lleva con query
    fun postLogin(@Query("email") email: String, @Query("password")password:String):
            Call<LoginResponse>

    @POST("logout")
    //Pasamos un parametro sobre la consulta eso se lleva con query
    fun postLogout(@Header("Authorization") authHeader: String): Call<Void>

    @POST("register")
    @Headers("Accept: application/json")
    fun postRegister(

        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("password_confirmation") password_confirmation: String,
        @Query("phone") phone: String
    ):Call<LoginResponse>

    @POST("fcm/token")
    @Headers("Accept: application/json")
    fun postToken(
        @Header("Authorization") authHeader: String,
        @Query("device_token") token: String
    ): Call<Void>



    @GET("id/remolienda")
    fun getRemolienda() : Call<ArrayList<Remolienda>>


    @POST("remolienda")
    @Headers("Accept: application/json")
    fun storeRemolienda(@Header("Authorization") authHeader: String,

                        @Query("name") name: String,
                        @Query("date") date: String,
                        @Query("muestra") muestra: String,
                        @Query("vertimil") vertimil: String,
                        @Query("hora") hora: String,
                        @Query("identify") identify: String,
                        @Query("ave") ave: String,
                        @Query("sd") sd: String,
                        @Query("density") density: String,
                        @Query("solidopuro") solidopuro: String,
                        @Query("m200") m200: String,
                        @Query("m400") m400: String
    ): Call<SimpleResponse>



    @GET("id/molienda")
    fun getMolienda() : Call<ArrayList<Molienda>>


    @POST("molienda")
    @Headers("Accept: application/json")
    fun storeMolienda(@Header("Authorization") authHeader: String,

                        @Query("name") name: String,
                        @Query("date") date: String,
                        @Query("muestra") muestra: String,
                        @Query("vertimil") vertimil: String,
                        @Query("hora") hora: String,
                        @Query("identify") identify: String,
                        @Query("ave") ave: String,
                        @Query("sd") sd: String,
                        @Query("density") density: String,
                        @Query("solidopuro") solidopuro: String,
                        @Query("m200") m200: String,
                        @Query("m400") m400: String
    ): Call<SimpleResponse>



    @GET("id/quinta")
    fun getQuinta() : Call<ArrayList<Quinta>>


    @POST("quinta")
    @Headers("Accept: application/json")
    fun storeQuinta(@Header("Authorization") authHeader: String,

                      @Query("name") name: String,
                      @Query("date") date: String,
                      @Query("muestra") muestra: String,
                      @Query("hora") hora: String,
                      @Query("identify") identify: String,
                      @Query("ave") ave: String,
                      @Query("sd") sd: String,
                      @Query("m48") m48: String,
                      @Query("m65") m65: String
    ): Call<SimpleResponse>




    @POST("norte")
    @Headers("Accept: application/json")
    fun storeNorte(@Header("Authorization") authHeader: String,

                    @Query("name") name: String,
                    @Query("date") date: String,
                    @Query("muestra") muestra: String,
                    @Query("hora") hora: String,
                    @Query("identify") identify: String,
                    @Query("codigolabquimico") codigo: String
    ): Call<SimpleResponse>

    @POST("moly")
    @Headers("Accept: application/json")
    fun storeMoly(@Header("Authorization") authHeader: String,

                   @Query("name") name: String,
                   @Query("date") date: String,
                   @Query("muestra") muestra: String,
                   @Query("hora") hora: String,
                   @Query("identify") identify: String,
                   @Query("codigolabquimico") codigo: String
    ): Call<SimpleResponse>


    @GET("id/remoliendac2")
    fun getRemoliendaC2() : Call<ArrayList<RemoliendaC2>>


    @POST("remolienda2")
    @Headers("Accept: application/json")
    fun storeRemoliendaC2(@Header("Authorization") authHeader: String,

                    @Query("name") name: String,
                    @Query("date") date: String,
                    @Query("muestra") muestra: String,
                    @Query("vertimil") vertimil: String,
                    @Query("hora") hora: String,
                    @Query("identify") identify: String,
                    @Query("ave") ave: String,
                    @Query("sd") sd: String,
                    @Query("density") density: String,
                    @Query("solidopuro") solidopuro: String,
                    @Query("m200") m200: String,
                    @Query("m400") m400: String
    ): Call<SimpleResponse>


    @GET("id/moliendac2")
    fun getMoliendaC2() : Call<ArrayList<MoliendaC2>>


    @POST("molienda2")
    @Headers("Accept: application/json")
    fun storeMoliendaC2(@Header("Authorization") authHeader: String,

                          @Query("name") name: String,
                          @Query("date") date: String,
                          @Query("muestra") muestra: String,
                          @Query("vertimil") vertimil: String,
                          @Query("hora") hora: String,
                          @Query("identify") identify: String,
                          @Query("ave") ave: String,
                          @Query("sd") sd: String,
                          @Query("density") density: String,
                          @Query("solidopuro") solidopuro: String,
                          @Query("m200") m200: String,
                          @Query("m400") m400: String
    ): Call<SimpleResponse>


    @POST("cobre")
    @Headers("Accept: application/json")
    fun storeCobre(@Header("Authorization") authHeader: String,

                  @Query("name") name: String,
                  @Query("date") date: String,
                  @Query("muestra") muestra: String,
                  @Query("hora") hora: String,
                  @Query("identify") identify: String,
                  @Query("codigolabquimico") codigo: String
    ): Call<SimpleResponse>

    companion object Factory{

        private const val BASE_URL ="http://64.227.107.185/api/"

        fun create(): ApiService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }

    }
}