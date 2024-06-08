package com.example.onlineappointmentsystem.fragment.doctor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlineappointmentsystem.R
import com.example.onlineappointmentsystem.SelectRoleActivity
import com.example.onlineappointmentsystem.databinding.FragmentDoctorProfileBinding
import com.example.onlineappointmentsystem.utils.SharedPrefrence

class DoctorProfileFragment : Fragment() {
     lateinit var binding: FragmentDoctorProfileBinding
    lateinit var session: SharedPrefrence
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoctorProfileBinding.inflate(inflater,container,false)
        session = SharedPrefrence(requireContext())
        binding.etFirstName.setText(session.getString(SharedPrefrence.USER_FIRST_NAME))
        binding.etLastName.setText(session.getString(SharedPrefrence.USER_LAST_NAME))
        binding.etMobileNumber.setText(session.getString(SharedPrefrence.MOBILE_NO))
        binding.etEmail.setText(session.getString(SharedPrefrence.EMAIL_ID))
        binding.etSpeciality.setText(session.getString(SharedPrefrence.SPECIALITY))
        binding.etExperience.setText(session.getString(SharedPrefrence.EXPERIENCE))
        binding.etEducation.setText(session.getString(SharedPrefrence.EDUCATION))
        binding.etAddress.setText(session.getString(SharedPrefrence.ADDRESS))
        binding.etPassword.setText(session.getString(SharedPrefrence.PASSWORD))

        binding.btnLogout.setOnClickListener {
            session.logOut()
            startActivity(Intent(requireContext(), SelectRoleActivity::class.java))
        }
        return binding.root
    }
}