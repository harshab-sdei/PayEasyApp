package com.example.peazy.webservices

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitForGoogleApi {
    companion object {
        val BASE_URL = "https://maps.googleapis.com/"


        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()

        }


        val apiService: WebServiceApi = getRetrofitInstance().create(WebServiceApi::class.java)


    }


}