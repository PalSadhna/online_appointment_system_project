package com.example.onlineappointmentsystem.Model

import android.net.Uri

data class DoctorSignupModel(val docId: Int,
    val docFirstName: String,
    val docLastName: String,
    val education:String,
    val speciality: String,
    val exp: String,
    val address: String,
    val mailId: String,
    val mobileNo: String,
    val password: String,
    val image: String)
