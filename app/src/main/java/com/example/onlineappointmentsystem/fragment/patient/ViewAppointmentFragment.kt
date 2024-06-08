package com.example.onlineappointmentsystem.fragment.patient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlineappointmentsystem.Model.AppointmentListModel
import com.example.onlineappointmentsystem.R
import com.example.onlineappointmentsystem.Utils
import com.example.onlineappointmentsystem.adapter.PatientAppointmentAdapter
import com.example.onlineappointmentsystem.databinding.FragmentPatientProfileBinding
import com.example.onlineappointmentsystem.databinding.FragmentViewAppointmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ViewAppointmentFragment : Fragment() {
    lateinit var binding: FragmentViewAppointmentBinding
    lateinit var appointmentList: ArrayList<AppointmentListModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewAppointmentBinding.inflate(inflater,container,false)
        appointmentList = ArrayList()
       // appointmentList.add(AppointmentListModel(1,"10/01/2023","2:30","fever","sadhna"))
        getAppointmentList()
        callRecycler()

        return binding.root
    }



    private fun getAppointmentList() {
        var mTheReference = FirebaseDatabase.getInstance().reference
        var mUsersReference = mTheReference.child("BookAppointment")

        mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.children.iterator().hasNext()) {
                    for (postSnapshot in dataSnapshot.children) {
                      val  appId = postSnapshot.child("appId").getValue().toString().toInt()
                      val  diseaseType = postSnapshot.child("diseaseType").getValue().toString()
                      val  doctorName = postSnapshot.child("doctorName").getValue().toString()
                      val  appDate = postSnapshot.child("appDate").getValue().toString()
                      val  appTime = postSnapshot.child("appTime").getValue().toString()
                        val patientName = postSnapshot.child("patientName").getValue().toString()
                        appointmentList.add(AppointmentListModel(appId,appDate,appTime,diseaseType,doctorName,patientName))
                        //AppointmentListModel(appId,appDate,appTime,diseaseType,doctorName)
                        callRecycler()
                    }

                } else {

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun callRecycler() {
        Log.d("sadhna",appointmentList.size.toString())
        if (appointmentList.isNotEmpty()){
            binding.recyclerAppointment.layoutManager = LinearLayoutManager(requireContext())
            appointmentList.reverse()
            binding.recyclerAppointment.adapter = PatientAppointmentAdapter(requireContext(),appointmentList)

        }
    }
}