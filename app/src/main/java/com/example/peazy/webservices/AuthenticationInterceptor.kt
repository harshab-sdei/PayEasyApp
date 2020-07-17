package com.example.peazy.webservices

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor(authToken: String) :Interceptor {
    private var authToken: String? = null


    init {
       this.authToken=authToken
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
            .header("Authorization", authToken!!)
        val request = builder.build()
        return chain.proceed(request)
    }
}
