package com.example.peazy.utility

import com.example.peazy.models.addcart.Add_Item
import java.util.ArrayList

class Constants {
    companion object {
        var addcartlist = ArrayList<Add_Item>()
        var vat: Double = 0.0
        var servicefree: Double = 5.0
        var tableNo: String = "00"
        var bar_id: String = ""
        var currency: String = "$"
        const val DATABASE_NAME = "payEasy_database."
        var email_error = "Email Address is invalid."
        var pws_error = "Password is not valid."
        var person_name_error = "Full Name is not correct."
        var card_number = "Please Enter valid Card Number."
        var valid_date = "Please Enter valid Date."
        var valid_CVV = "Please Enter valid CVV."
        var totalamount = "totalamount"
        var stripToken: String? = ""

        val IS_USER_Login: String = "is_user_login"
        val IS_USER_Choose_Mode: String = "is_user_choose_mode"

        /*API Parameters Name */
        var NAME: String = "name"
        var EMAIL: String = "email"
        var PASSWORD: String = "password"
        var LANGUAGE_SEL: String = "language_selected"
        var DEVICE_TOKEN: String = "device_token"
        var PAYMENT_MODE: String = "payment_mode"
        var ACCESS_TOKEN: String = "access_token"
        var USER_NAME: String = "user_name"
        var USER_EMAIL: String = "user_email"
        var PLATFORM: String = "platform"


        val googleapi: String = "AIzaSyBKum4xy87zoNTlYlXFJsGYFiXmL9aGW_k"


        const val ACCESS_FINE_LOCATION = "access fine location"
        const val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 101


        //path
        const val BAR_DETAIL_IMG_PATH = "bar_image_path"
        const val MENU_CAT_IMG_PATH = "menu_cat_img_path"
        const val MENU_ITEM_PATH = "menu_item_path"
    }
}