package com.example.peazy.webservices

import com.example.peazy.models.category.MenuCategory
import com.example.peazy.models.logout.Logout
import com.example.peazy.models.menu_item.BarMenuItem
import com.example.peazy.models.nearby.NearByBar
import com.example.peazy.models.signup.SignUP
import com.example.peazy.models.subcategory.SubCategory
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

    @FormUrlEncoded
    @POST("/api/v2/user/bar/nearby")
    suspend fun nearByBar(@Field("coordinate") coordinate: String): Response<NearByBar>


    @GET("/api/v2/category/list")
    suspend fun getCategoryList(@QueryMap params: Map<String, String>): Response<MenuCategory>

    @GET("{fullUrl}")
    suspend fun getSubCategory(
        @Path(value = "fullUrl", encoded = true) fullUrl: String,
        @QueryMap params: Map<String, String>
    ): Response<SubCategory>

    @GET(" /api/v2/item/list")
    suspend fun getMenuItem(@QueryMap params: Map<String, String>): Response<BarMenuItem>

}