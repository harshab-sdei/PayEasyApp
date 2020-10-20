package com.peazyapp.peazy.webservices

import retrofit2.http.Field

class ApiHelper(private val apiService: WebServiceApi) {
    suspend fun signupUser(@Field("name") name:String, @Field("email") email:String, @Field("password") pws:String)=apiService.signupUser(name,email,pws)


}