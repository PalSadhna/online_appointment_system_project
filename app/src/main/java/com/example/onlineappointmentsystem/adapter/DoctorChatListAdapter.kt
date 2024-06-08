package com.example.onlineappointmentsystem.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.onlineappointmentsystem.Model.AppointmentListModel
import com.example.onlineappointmentsystem.Model.getPatientList
import com.example.onlineappointmentsystem.OneToOneChatActivity
import com.example.onlineappointmentsystem.R
import com.example.onlineappointmentsystem.databinding.ChatListRecyclerItemsBinding
import com.example.onlineappointmentsystem.databinding.ScheduleAppointmentLayoutBinding

class DoctorChatListAdapter(val context: Context, val arrayList: ArrayList<getPatientList>):RecyclerView.Adapter<DoctorChatListAdapter.ViewHolder>() {
    class ViewHolder(val binding: ChatListRecyclerItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChatListRecyclerItemsBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
     return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
with(holder){
    with(arrayList[position]){
     binding.name.text = this.PName
        Glide.with(context).load(this.PImage).placeholder(R.drawable.chat_profile_pic).error(R.drawable.chat_profile_pic).into(binding.profilePic);
    binding.llSingleItem.setOnClickListener {
        val intent = Intent(context,OneToOneChatActivity::class.java)
        intent.putExtra("receiverId",this.patientId)
        intent.putExtra("receiverName",this.PName)
        intent.putExtra("pImage",this.PImage)
        context.startActivity(intent)
    }
    }
}
    }
}