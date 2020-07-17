package com.example.peazy.webservices

import com.example.peazy.models.signup.SignUP
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FieldMap

class ApiHelper(private val apiService: WebServiceApi) {
    suspend fun signupUser(@Field("name") name:String, @Field("email") email:String, @Field("password") pws:String)=apiService.signupUser(name,email,pws)


}