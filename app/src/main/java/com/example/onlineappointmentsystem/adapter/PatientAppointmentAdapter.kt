package com.example.onlineappointmentsystem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineappointmentsystem.Model.AppointmentListModel
import com.example.onlineappointmentsystem.databinding.PatientAppointmentRecyclerItemBinding

class PatientAppointmentAdapter(val context: Context, val arrayList: ArrayList<AppointmentListModel>):RecyclerView.Adapter<PatientAppointmentAdapter.ViewHolder>() {
    class ViewHolder(val binding: PatientAppointmentRecyclerItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PatientAppointmentRecyclerItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(arrayList[position]){
                binding.txtDiseaseName.text = this.diseaseType
                binding.txtDoctorName.text = this.doctorName
                binding.txtAppTime.text = this.appTime
                binding.txtappDate.text = this.appDate
            }
        }
    }
}