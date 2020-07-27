package com.example.peazy.webservices

import com.example.peazy.utility.Constants
import com.example.peazy.utility.appconfig.UserPreferenc
import okhttp3.Credentials
import okhttp3.Interceptor

class BasicAuthInterceptor(s: String, s1: String) :Interceptor{
    private var credentials: String = Credentials.basic(s, s1)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        var builder = request.newBuilder()
            .header("Authorization", credentials)
            .addHeader(Constants.LANGUAGE_SEL, "1")
            .addHeader(Constants.PLATFORM, "1")
            .addHeader(
                Constants.DEVICE_TOKEN,
                "" + UserPreferenc.getStringPreference(Constants.DEVICE_TOKEN, "")
            )
            .addHeader(
                "accessToken",
                "" + UserPreferenc.getStringPreference(Constants.ACCESS_TOKEN, "")
            )

        val request1 = builder.build()
        return chain.proceed(request1)
    }
}





