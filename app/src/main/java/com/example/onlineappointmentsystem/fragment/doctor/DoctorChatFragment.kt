package com.example.onlineappointmentsystem.fragment.doctor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlineappointmentsystem.Model.getPatientList
import com.example.onlineappointmentsystem.R
import com.example.onlineappointmentsystem.adapter.DoctorChatListAdapter
import com.example.onlineappointmentsystem.databinding.FragmentDoctorChatBinding
import com.example.onlineappointmentsystem.utils.SharedPrefrence
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DoctorChatFragment : Fragment() {
   lateinit var binding: FragmentDoctorChatBinding
   val patientList = ArrayList<getPatientList>()
    lateinit var sharedPrefrence: SharedPrefrence

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorChatBinding.inflate(inflater,container,false)
        sharedPrefrence = SharedPrefrence(requireContext())
        getPatientList()
        return binding.root
    }

    private fun getPatientList() {
        var mTheReference = FirebaseDatabase.getInstance().reference
        val loginType = sharedPrefrence.getString(SharedPrefrence.LOGINTYPE)
        var mUsersReference:DatabaseReference
        if (loginType.equals("doctor")){
            mUsersReference = mTheReference.child("PatientSignup")
            mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.children.iterator().hasNext()) {
                        for (postSnapshot in dataSnapshot.children) {
                            val patientId = postSnapshot.child("pid").getValue().toString()
                            val patientName = postSnapshot.child("firstName").getValue().toString() + " " + postSnapshot.child("lastName").getValue().toString()
                            val patientImage = postSnapshot.child("image").getValue().toString()
                            patientList.add(getPatientList(patientId,patientName,patientImage))
                        }
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.adapter = DoctorChatListAdapter(requireContext(),patientList)
                    } else {
                        // userSignupDataBase()
                        Log.d("OnlineAppointentSystem", "No Data")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }else{
            mUsersReference = mTheReference.child("DoctorSignup")
            mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.children.iterator().hasNext()) {
                        for (postSnapshot in dataSnapshot.children) {
                            val patientId = postSnapshot.child("docId").getValue().toString()
                            val patientName = postSnapshot.child("docFirstName").getValue().toString() + " " + postSnapshot.child("docLastName").getValue().toString()
                            val patientImage = postSnapshot.child("image").getValue().toString()
                            patientList.add(getPatientList(patientId,patientName,patientImage))
                        }
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.adapter = DoctorChatListAdapter(requireContext(),patientList)
                    } else {
                        // userSignupDataBase()
                        Log.d("OnlineAppointentSystem", "No Data")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }


    }
}