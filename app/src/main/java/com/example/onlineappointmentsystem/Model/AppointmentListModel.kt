package com.example.onlineappointmentsystem.Model

data class AppointmentListModel(val appId: Int,
                                val appDate: String,
                                val appTime: String,
                                val diseaseType: String,
                                val doctorName: String,
                                val patientName: String )
