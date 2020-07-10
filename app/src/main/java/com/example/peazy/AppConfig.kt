package com.example.peazy

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class AppConfig :Application(){
    companion object {
        var mInstance: AppConfig? = null


        private var sharedPreferences: SharedPreferences? = null
        private var sharedPreferencesEditor: SharedPreferences.Editor? = null
        private var mContext: Context? = null

        @Synchronized
        fun getInstance(): AppConfig? {
            return mInstance
        }

        fun getContext(): Context? {
            return mContext
        }

        fun setContext(mctx: Context?) {
            mContext = mctx
        }

        fun getApplicationPreferenceEditor(): SharedPreferences.Editor? {
            if (sharedPreferencesEditor == null) sharedPreferencesEditor =
                sharedPreferences!!.edit()
            return sharedPreferencesEditor
        }

        fun getApplicationPreference(): SharedPreferences? {
            if (sharedPreferences == null) sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext())
            return sharedPreferences
        }



   }

    override fun onCreate() {
        super.onCreate()

        mInstance = this
        try {
            sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
            sharedPreferencesEditor = Companion.sharedPreferences?.edit()
            setContext(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}