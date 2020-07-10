package com.example.peazy.utility.appconfig

import com.example.peazy.AppConfig

class UserPreferenc {
    companion object {
        fun SetIntegerPreference(key: String?, value: Int) {
            AppConfig.getApplicationPreferenceEditor()!!.putInt(key, value)
            AppConfig.getApplicationPreferenceEditor()!!.commit()
        }

        fun getIntegerPreference(key: String?, defaultValue: Int): Int {
            return AppConfig.getApplicationPreference()!!.getInt(key, defaultValue)
        }

        fun setBooleanPreference(key: String?, value: Boolean) {
            AppConfig.getApplicationPreferenceEditor()!!.putBoolean(key, value)
            AppConfig.getApplicationPreferenceEditor()!!.commit()
        }

        fun getBooleanPreference(
            key: String?,
            defaultValue: Boolean
        ): Boolean {
            return AppConfig.getApplicationPreference()!!.getBoolean(key, defaultValue)
        }

        fun setStringPreference(key: String?, value: String?) {
            AppConfig.getApplicationPreferenceEditor()!!.putString(key, value)
            AppConfig.getApplicationPreferenceEditor()!!.commit()
        }

        fun getStringPreference(
            key: String?,
            defaultValue: String?
        ): String? {
            return AppConfig.getApplicationPreference()!!.getString(key, defaultValue)
        }

        fun setLongPreference(key: String?, value: Long) {
            AppConfig.getApplicationPreferenceEditor()!!.putLong(key, value)
            AppConfig.getApplicationPreferenceEditor()!!.commit()
        }

        fun getLongPreference(key: String?, defaultValue: Long): Long {
            return AppConfig.getApplicationPreference()!!.getLong(key, defaultValue)
        }

        fun preferenceRemoveKey(key: String?) {
            AppConfig.getApplicationPreferenceEditor()!!.remove(key)
            AppConfig.getApplicationPreferenceEditor()!!.commit()
        }

        fun clearPreference() {
            AppConfig.getApplicationPreferenceEditor()!!.clear()
            AppConfig.getApplicationPreferenceEditor()!!.commit()
        }

        fun contains(key: String?): Boolean? {
            return AppConfig.getApplicationPreference()!!.contains(key)
        }

        operator fun get(key: String?): String? {
            return AppConfig.getApplicationPreference()!!.getString(key, null)
        }

        fun save(key: String?, value: String?) {
            AppConfig.getApplicationPreferenceEditor()!!.putString(key, value)
            AppConfig.getApplicationPreferenceEditor()!!.commit()
        }

    }
}