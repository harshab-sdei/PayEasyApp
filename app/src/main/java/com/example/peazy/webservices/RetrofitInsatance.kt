package com.example.peazy.webservices

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInsatance {

    companion object {
        val BASE_URL = "http://54.190.192.105:6050"
        val client = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor("peasyApp", "@:{peasy!#App-=^!}"))
            .build()


        fun getRetrofitInstance(): Retrofit
        {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()

        }


        val apiService: WebServiceApi = getRetrofitInstance().create(WebServiceApi::class.java)



    }


}





