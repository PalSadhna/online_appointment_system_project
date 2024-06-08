package com.example.onlineappointmentsystem.fragment.patient

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlineappointmentsystem.SelectRoleActivity
import com.example.onlineappointmentsystem.databinding.FragmentPatientProfileBinding
import com.example.onlineappointmentsystem.utils.SharedPrefrence

class PatientProfileFragment : Fragment() {
    lateinit var binding: FragmentPatientProfileBinding
    lateinit var session: SharedPrefrence

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientProfileBinding.inflate(inflater,container,false)
        session = SharedPrefrence(requireContext())
        binding.etFirstName.setText(session.getString(SharedPrefrence.USER_FIRST_NAME))
        binding.etLastName.setText(session.getString(SharedPrefrence.USER_LAST_NAME))
        binding.etMobileNumber.setText(session.getString(SharedPrefrence.MOBILE_NO))
        binding.etEmail.setText(session.getString(SharedPrefrence.EMAIL_ID))

        binding.btnLogout.setOnClickListener {
            session.logOut()
            startActivity(Intent(requireContext(),SelectRoleActivity::class.java))
        }
        return binding.root
    }


}