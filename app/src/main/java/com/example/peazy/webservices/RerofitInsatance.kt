package com.example.peazy.webservices

import android.text.TextUtils
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RerofitInsatance {

    companion object
    {
              val BASE_URL="http://54.190.192.105:6050"
        val client =  OkHttpClient.Builder()
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





