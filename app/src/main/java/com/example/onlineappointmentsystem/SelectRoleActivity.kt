package com.example.onlineappointmentsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.se.omapi.Session
import com.example.onlineappointmentsystem.databinding.ActivitySelectRoleBinding
import com.example.onlineappointmentsystem.utils.SharedPrefrence

class SelectRoleActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectRoleBinding
    lateinit var session:SharedPrefrence
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SharedPrefrence(this)
        if (session.getBoolean(SharedPrefrence.IS_LOGIN)){
            if (session.getString(SharedPrefrence.LOGINTYPE) == "patient"){
                startActivity(Intent(this,PatientHomeActivity::class.java))
            }else{
                startActivity(Intent(this,DoctorHomeActivity::class.java))
            }

        }else{

            binding.llAdminHr.setOnClickListener {
                val intent = Intent(this,DoctorSignupActivity::class.java)
                intent.putExtra("userType","Doctor")
                startActivity(intent)
            }
            binding.llEmployee.setOnClickListener {
                val intent = Intent(this,PatientSignupActivity::class.java)
                intent.putExtra("userType","Patient")
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}