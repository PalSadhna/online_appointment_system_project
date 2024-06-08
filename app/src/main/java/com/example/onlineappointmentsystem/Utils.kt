package com.example.onlineappointmentsystem

import android.content.Context
import android.util.Patterns
import android.widget.Toast

object Utils {
    fun showToast(context: Context,msg: String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}