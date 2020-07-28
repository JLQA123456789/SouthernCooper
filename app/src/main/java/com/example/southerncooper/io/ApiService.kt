package com.example.southerncooper.io

import com.example.southerncooper.io.Response.LoginResponse
import com.example.southerncooper.model.Remolienda
import com.example.southerncooper.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("id/remolienda")
     fun getRemolienda() : Call<ArrayList<Remolienda>>


    @POST("login")
    //Pasamos un parametro sobre la consulta eso se lleva con query
    fun postLogin(@Query("email") email: String, @Query("password")password:String):
            Call<LoginResponse>

    @POST("logout")
    //Pasamos un parametro sobre la consulta eso se lleva con query
    fun postLogout(@Header("Authorization") authHeader: String): Call<Void>


    companion object Factory{

        private const val BASE_URL ="http://104.131.27.228/api/"

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