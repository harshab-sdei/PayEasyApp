package com.example.peazy.utility

class Constants {
    companion object {
     const val DATABASE_NAME = "payEasy_database."
      val nearByplaceAPI="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=658964962677-c0ui9tlef7688tu2raijhq1mhub28saj.apps.googleusercontent.com"

     var email_error="Email Address is invalid."
     var pws_error="Password is not valid."
     var person_name_error="Full Name is not correct."

     val IS_USER_Login:String="is_user_login"

     /*API Parameters Name */
      var NAME:String="name"
      var EMAIL:String="email"
      var PASSWORD:String="password"
      var LANGUAGE_SEL:String="language_selected"
      var DEVICE_TOKEN:String="device_token"
      var PLATFORM:String="platform"
    }
}