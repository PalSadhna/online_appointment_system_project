package com.example.onlineappointmentsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.onlineappointmentsystem.databinding.ActivityDoctorHomeBinding
import com.example.onlineappointmentsystem.fragment.doctor.DoctorChatFragment
import com.example.onlineappointmentsystem.fragment.doctor.DoctorProfileFragment
import com.example.onlineappointmentsystem.fragment.doctor.ScheduleAppointemntFragment
import com.example.onlineappointmentsystem.fragment.patient.BookAppointmentFragment
import com.example.onlineappointmentsystem.fragment.patient.PatientProfileFragment
import com.example.onlineappointmentsystem.fragment.patient.ViewAppointmentFragment

class DoctorHomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityDoctorHomeBinding
    lateinit var meowBottomNavigation: MeowBottomNavigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Bottom Navigation
        meowBottomNavigation = findViewById(R.id.doctorBottomNavigation)
        meowBottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.book_appointment_icon))
        meowBottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.view_appointment))
        meowBottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.profile))
        meowBottomNavigation.add(MeowBottomNavigation.Model(4,R.drawable.chat_icon))
        replaceFragment(ScheduleAppointemntFragment(),0)

        meowBottomNavigation.show(1, true)
        meowBottomNavigation.setOnClickMenuListener { model: MeowBottomNavigation.Model? ->
            when (model?.id) {
                1 -> replaceFragment(ScheduleAppointemntFragment(), 1)
                2 -> replaceFragment(com.example.onlineappointmentsystem.fragment.doctor.ViewAppointmentFragment(),1)
                3 -> replaceFragment(DoctorProfileFragment(), 1)
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