package com.peazyapp.peazy.webservices

import com.peazyapp.peazy.models.addpaycard.AddPayCard
import com.peazyapp.peazy.models.booktable.BookTable
import com.peazyapp.peazy.models.category.MenuCategory
import com.peazyapp.peazy.models.changepws.ChangePWS
import com.peazyapp.peazy.models.editprofile.EditProfile
import com.peazyapp.peazy.models.logout.Logout
import com.peazyapp.peazy.models.menu_item.MenuItems
import com.peazyapp.peazy.models.nearby.NearByBar
import com.peazyapp.peazy.models.oderhistory.OrderHistory
import com.peazyapp.peazy.models.payorder.PayOrder
import com.peazyapp.peazy.models.reserve_table.ReserveTable
import com.peazyapp.peazy.models.signup.SignUP
import com.peazyapp.peazy.models.subcategory.SubCategory
import com.peazyapp.peazy.models.subsubcategory.SubSubCategory
import com.peazyapp.peazy.models.verifypay.VerifyPay
import com.peazyapp.peazy.models.viewcard.ViewCard
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

    @FormUrlEncoded
    @PUT("/api/v2/user/change-password")
    suspend fun changePws(@FieldMap params: Map<String, String>): Response<ChangePWS>

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
    @PUT("/api/v2/user/change/reserve/table")
    suspend fun changeReserveTable(@FieldMap params: Map<String, String>): Response<ReserveTable>

    @FormUrlEncoded
    @POST("/api/v2/user/card/add")
    suspend fun addCard(@FieldMap params: Map<String, String>): Response<AddPayCard>

    @GET("/api/v2/user/card/view")
    suspend fun viewCard(): Response<ViewCard>


    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("/api/v2/user/pay")
    suspend fun payOrder(@FieldMap params: Map<String, Any?>): Response<PayOrder>

    @FormUrlEncoded
    @POST("/api/v2/user/verify/pay")
    suspend fun verifyPay(@Field("stripe_token") striptoken: String): Response<VerifyPay>


    @GET("/api/v2/category/list")
    suspend fun getCategoryList(@QueryMap params: Map<String, String>): Response<MenuCategory>

    @GET("{fullUrl}")
    suspend fun getSubCategory(
        @Path(value = "fullUrl", encoded = true) fullUrl: String,
        @QueryMap params: Map<String, String>
    ): Response<SubCategory>

    @GET("{fullUrl}")
    suspend fun getSubSubCategory(
        @Path(value = "fullUrl", encoded = true) fullUrl: String,
        @QueryMap params: Map<String, String>
    ): Response<SubSubCategory>

    @GET(" /api/v2/item/list")
    suspend fun getMenuItem(@QueryMap params: Map<String, String>): Response<MenuItems>

    @GET("/api/v2/user/order/history/list")
    suspend fun getHistoryList(): Response<OrderHistory>

}