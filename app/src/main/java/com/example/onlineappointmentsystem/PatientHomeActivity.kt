package com.example.onlineappointmentsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.onlineappointmentsystem.databinding.ActivityPatientHomeBinding
import com.example.onlineappointmentsystem.fragment.doctor.DoctorChatFragment
import com.example.onlineappointmentsystem.fragment.patient.BookAppointmentFragment
import com.example.onlineappointmentsystem.fragment.patient.PatientProfileFragment
import com.example.onlineappointmentsystem.fragment.patient.ViewAppointmentFragment

class PatientHomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityPatientHomeBinding
    lateinit var meowBottomNavigation: MeowBottomNavigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Navigation
        meowBottomNavigation = findViewById(R.id.bottomNavigation)
        meowBottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.book_appointment_icon))
        meowBottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.view_appointment))
        meowBottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.profile))
        meowBottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.chat_icon))
        replaceFragment(BookAppointmentFragment(),0)

        meowBottomNavigation.show(1, true)
        meowBottomNavigation.setOnClickMenuListener { model: MeowBottomNavigation.Model? ->
            when (model?.id) {
                1 -> replaceFragment(BookAppointmentFragment(), 1)
                2 -> replaceFragment(ViewAppointmentFragment(),1)
                3 -> replaceFragment(PatientProfileFragment(), 1)
                4-> replaceFragment(DoctorChatFragment(),1)

            }


        }
    }
     fun replaceFragment(fragment : Fragment, flag: Int){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if(flag == 0){
            fragmentTransaction.add(R.id.fragmentContainer, fragment)

        }else{
            fragmentTransaction.replace(R.id.fragmentContainer,fragment)

        }
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
       finishAffinity()
    }

}