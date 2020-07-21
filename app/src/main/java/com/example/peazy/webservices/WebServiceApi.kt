package com.example.peazy.webservices

import com.example.peazy.models.logout.Logout
import com.example.peazy.models.nearbyplace.NearByPlace
import com.example.peazy.models.signup.SignUP
import com.example.peazy.utility.Constants
import com.example.peazy.utility.Constants.Companion.googleapi
import retrofit2.Response
import retrofit2.http.*

interface WebServiceApi {


    @FormUrlEncoded
    @POST("/api/v2/user/signup")
    suspend fun signupUser(@Field("name") name:String,@Field("email") email:String, @Field("password") pws:String): Response<SignUP>

    @FormUrlEncoded
    @POST("/api/v2/user/login")
    suspend fun loginUser(@FieldMap params: Map<String, String>): Response<SignUP>

    @FormUrlEncoded
    @POST("/api/v2/user/forget/password")
    suspend fun fogotPassword(@Field("email") email: String): Response<SignUP>

    @POST("/api/v2/user/logout")
    suspend fun logoutUser(): Response<Logout>

    @GET("maps/api/place/textsearch/json?location=-33.8670522,151.1957362&radius=500&types=food&key=AIzaSyDSrjisanRjRflfR_B4ukdHBqknkezF2LE")
    suspend fun findNearByPlace(): Response<NearByPlace>

}