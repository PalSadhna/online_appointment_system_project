package com.example.onlineappointmentsystem.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefrence(val context: Context) {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    companion object {
        const val PREFERENCE_NAME = "AppointmentSystem"
        const val IS_LOGIN = "is_login"
        const val USER_FIRST_NAME = "user_first_name"
        const val USER_LAST_NAME = "user_last_name"
        const val MOBILE_NO = "mobile_no"
        const val EMAIL_ID = "email_id"
        const val PASSWORD = "password"
        const val EDUCATION = "education"
        const val SPECIALITY = "speciality"
        const val EXPERIENCE = "experience"
        const val ADDRESS = "address"
        const val LOGINTYPE = "login_type"
        const val LOGINID = "login_Id"
    }

    fun setInt(key: String, value: Int){
        preferences = context.getSharedPreferences(PREFERENCE_NAME, 0)
        editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String) : Int{
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getInt(key,-1)
    }

    // set the string value in SharedPreferences
    fun setString(PREF_NAME: String?, value: String?) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, 0)
        // edit the editor to put values in shared preference
        editor = preferences.edit()
        editor.putString(PREF_NAME, value)
        editor.commit()
    }

    fun getString(PREF_NAME: String): String? {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, 0)
        // check if SharedPreferences contains the value or not
        return if (preferences.contains(PREF_NAME)) {
            // return the stored value
            preferences.getString(PREF_NAME, "")
        } else ""
        // if SharedPreferences not contains the value then return empty string
    }

    fun setBoolean(PREF_NAME: String?, value: Boolean) {
        // PREF_NAME is the key to store
        // value is value for key
        preferences = context.getSharedPreferences(PREFERENCE_NAME, 0)
        editor = preferences.edit()
        // set the value in SharedPreferences
        editor.putBoolean(PREF_NAME, value)
        // commit the editor to make changes in the SharedPreferences
        editor.commit()
    }

    fun getBoolean(PREF_NAME: String?): Boolean {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, 0)
        // edit the editor to put values in shared preference
        return preferences.getBoolean(PREF_NAME, false)
    }

    fun logOut(){
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit()
        editor.clear()
        editor.apply()
    }


}