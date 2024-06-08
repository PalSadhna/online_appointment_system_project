package com.example.onlineappointmentsystem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineappointmentsystem.Model.AppointmentListModel
import com.example.onlineappointmentsystem.databinding.ScheduleAppointmentLayoutBinding
import com.example.onlineappointmentsystem.databinding.UpcomingAppRecyclerItemBinding

class UpcomingAppAdapter(val context: Context, val arrayList: ArrayList<AppointmentListModel>): RecyclerView.Adapter<UpcomingAppAdapter.ViewHolder>() {
    class ViewHolder(val binding: UpcomingAppRecyclerItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingAppAdapter.ViewHolder {
        val binding = UpcomingAppRecyclerItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: UpcomingAppAdapter.ViewHolder, position: Int) {
       with(holder){
           with(arrayList[position]){
               binding.txtAppTime.text = this.appTime
               binding.txtappDate.text = this.appDate
               binding.txtDiseaseName.text = this.diseaseType
           }
       }
    }
}