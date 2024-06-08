package com.example.onlineappointmentsystem.fragment.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlineappointmentsystem.Model.AppointmentListModel
import com.example.onlineappointmentsystem.R
import com.example.onlineappointmentsystem.adapter.UpcomingAppAdapter
import com.example.onlineappointmentsystem.databinding.FragmentViewAppointment2Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.text.SimpleDateFormat
import java.util.Date

class ViewAppointmentFragment : Fragment() {

    lateinit var binding: FragmentViewAppointment2Binding
    lateinit var upcomingAppointmentList: ArrayList<AppointmentListModel>
    lateinit var pastAppointmentList: ArrayList<AppointmentListModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewAppointment2Binding.inflate(inflater,container,false)
        upcomingAppointmentList = ArrayList()
        pastAppointmentList = ArrayList()
        binding.lytOwnRating.setOnClickListener {
            upcomingAppointment()
        }
        binding.txtTeamRating.setOnClickListener {
            pastAppointment()
        }

        dataSetRecyclerview()
        return binding.root

    }

    private fun dataSetRecyclerview() {
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
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val strDate: Date? = sdf.parse(appDate)
                        if (strDate != null) {
                            if (System.currentTimeMillis() < strDate.time) {
                                upcomingAppointmentList.add(AppointmentListModel(appId,appDate,appTime,diseaseType,doctorName,patientName))
                            }
                            if (upcomingAppointmentList.isNotEmpty()){
                                binding.upcomingRecyclerview.visibility = View.VISIBLE
                                binding.upcomingNoData.visibility = View.GONE
                                binding.upcomingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                                binding.upcomingRecyclerview.adapter = UpcomingAppAdapter(requireContext(),upcomingAppointmentList)
                            }else{
                                binding.upcomingRecyclerview.visibility = View.GONE
                                binding.upcomingNoData.visibility = View.VISIBLE
                            }
                            if (System.currentTimeMillis() > strDate.time) {
                                pastAppointmentList.add(AppointmentListModel(appId,appDate,appTime,diseaseType,doctorName,patientName))
                            }
                            if (pastAppointmentList.isNotEmpty()){
                                binding.pastRecyclerview.visibility = View.VISIBLE
                                binding.pastNoData.visibility = View.GONE
                                binding.upcomingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                                binding.upcomingRecyclerview.adapter = UpcomingAppAdapter(requireContext(),pastAppointmentList)
                            }else{
                                binding.pastRecyclerview.visibility = View.GONE
                                binding.pastNoData.visibility = View.VISIBLE
                            }
                        }
                    }

                } else {

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun pastAppointment() {
        binding.lytOwnRating.setBackgroundResource(R.drawable.toggle_widget_background)
        binding.txtOwnRating.setTextColor(resources.getColor(R.color.switchTextColor))
        binding.lytTeamRating.setBackgroundResource(R.drawable.toggle_button_green)
        binding.txtTeamRating.setTextColor(resources.getColor(R.color.white))
        binding.lytUpcoming.visibility = View.GONE
        binding.lytPast.visibility = View.VISIBLE
    }

    private fun upcomingAppointment() {
        binding.lytOwnRating.setBackgroundResource(R.drawable.toggle_button_green)
        binding.txtOwnRating.setTextColor(resources.getColor(R.color.white))
        binding.lytTeamRating.setBackgroundResource(R.drawable.toggle_widget_background)
        binding.txtTeamRating.setTextColor(resources.getColor(R.color.switchTextColor))
        binding.lytUpcoming.visibility = View.VISIBLE
        binding.lytPast.visibility = View.GONE

    }

}