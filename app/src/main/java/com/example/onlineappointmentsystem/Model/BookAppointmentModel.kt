package com.example.onlineappointmentsystem.Model

data class BookAppointmentModel(
    val AppId: Int,
    val PatientName: String,
    val DiseaseType: String,
    val DoctorName: String,
    val AppDate: String,
    val AppTime: String)
