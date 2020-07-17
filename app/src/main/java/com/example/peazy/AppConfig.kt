package com.example.peazy

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class AppConfig :Application(){

    companion object {
        var mInstance: AppConfig? = null


        lateinit var sharedPreferences: SharedPreferences
        lateinit var sharedPreferencesEditor: SharedPreferences.Editor
        lateinit var mContext: Context

        @Synchronized
        fun getInstance(): AppConfig? {
            return mInstance
        }

        fun getContext(): Context? {
            return mContext
        }

        fun setContext(context: Context)
        {
            mContext=context
        }


        fun getApplicationPreferenceEditor(): SharedPreferences.Editor? {
            if (sharedPreferencesEditor == null) {
                sharedPreferences =
                    getContext()!!.getSharedPreferences("peazy.txt", Context.MODE_PRIVATE)
                sharedPreferencesEditor = sharedPreferences?.edit()
            }
            return sharedPreferencesEditor
        }

        fun getApplicationPreference(): SharedPreferences? {
            if (sharedPreferences == null) sharedPreferences =
                getContext()!!.getSharedPreferences("peazy.txt",Context.MODE_PRIVATE)
            return sharedPreferences
        }



   }


    override fun onCreate() {
        super.onCreate()

        mInstance = this
        try {
            setContext(applicationContext)
            sharedPreferences =
                applicationContext!!.getSharedPreferences("peazy.txt",Context.MODE_PRIVATE)
            sharedPreferencesEditor = Companion.sharedPreferences?.edit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}