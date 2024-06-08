package com.example.onlineappointmentsystem.Model

data class PatientSignupModel(
    val pId: Int,
    val firstName: String,
    val lastName: String,
    val mailId: String,
    val MobileNo: String,
    val Password: String,
    val Image: String)