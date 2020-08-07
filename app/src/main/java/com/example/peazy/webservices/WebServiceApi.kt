package com.example.peazy.webservices

import com.example.peazy.models.addpaycard.AddPayCard
import com.example.peazy.models.booktable.BookTable
import com.example.peazy.models.category.MenuCategory
import com.example.peazy.models.editprofile.EditProfile
import com.example.peazy.models.logout.Logout
import com.example.peazy.models.menu_item.BarMenuItem
import com.example.peazy.models.nearby.NearByBar
import com.example.peazy.models.reserve_table.ReserveTable
import com.example.peazy.models.signup.SignUP
import com.example.peazy.models.subcategory.SubCategory
import retrofit2.Response
import retrofit2.http.*

interface WebServiceApi {


    @FormUrlEncoded
    @POST("/api/v2/user/signup")
    suspend fun signupUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pws: String
    ): Response<SignUP>

    @FormUrlEncoded
    @POST("/api/v2/user/login")
    suspend fun loginUser(@FieldMap params: Map<String, String>): Response<SignUP>

    @FormUrlEncoded
    @POST("/api/v2/user/forget/password")
    suspend fun fogotPassword(@Field("email") email: String): Response<SignUP>

    @FormUrlEncoded
    @PUT("/api/v2/user/edit")
    suspend fun editProfile(@FieldMap params: Map<String, String>): Response<EditProfile>

    @DELETE("/api/v2/user/logout")
    suspend fun logoutUser(): Response<Logout>

    @FormUrlEncoded
    @POST("/api/v2/user/bar/nearby")
    suspend fun nearByBar(@Field("coordinate") coordinate: String): Response<NearByBar>

    @FormUrlEncoded
    @POST("/api/v2/user/book/table")
    suspend fun bookTable(@FieldMap params: Map<String, String>): Response<BookTable>

    @FormUrlEncoded
    @POST("/api/v2/user/reserve/table")
    suspend fun reserveTable(@FieldMap params: Map<String, String>): Response<ReserveTable>

    @FormUrlEncoded
    @POST("/api/v2/user/card/add")
    suspend fun addCard(@FieldMap params: Map<String, String>): Response<AddPayCard>

    @FormUrlEncoded
    @POST("/api/v2/user/reserve/table")
    suspend fun pay(@FieldMap params: Map<String, String>): Response<AddPayCard>


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