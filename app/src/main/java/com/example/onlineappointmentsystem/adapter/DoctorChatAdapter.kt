package com.example.onlineappointmentsystem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineappointmentsystem.Model.ChatListResposne
import com.example.onlineappointmentsystem.R

class DoctorChatAdapter(val context: Context,val chatList: ArrayList<ChatListResposne>,val userId:String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEVIE = 1
    val ITEM_SENT = 2
    val ITEM_SUPPORT = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            val view = LayoutInflater.from(context).inflate(R.layout.receive_message,parent,false)
            return ReceiveViewHolder(view)
        }else if (viewType == 3){
            val view = LayoutInflater.from(context).inflate(R.layout.chat_support_detail_message,parent,false)
            return SupportViewHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.send_message,parent,false)
            return SentViewHolder(view)
        }    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = chatList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sent_Message?.text = currentMessage.msg
        }else if (holder.javaClass == SupportViewHolder::class.java){
            val viewHolder = holder as SupportViewHolder
            holder.supportMessage.text = currentMessage.msg
        }else{
            val viewHolder = holder as ReceiveViewHolder
            val textData = currentMessage
            holder.receive_Message?.text = textData.msg

        }

    }

    override fun getItemCount(): Int {

        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = chatList[position]

        if (currentMessage.msgType == "receiver") {
                return ITEM_SENT
        }else{
            return ITEM_RECEVIE
        }
    }

    class SentViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView){
        val sent_Message = itemView.findViewById<TextView>(R.id.txt_sent_message)

    }
    class ReceiveViewHolder(itemView: android.view.View): RecyclerView.ViewHolder(itemView){
        val receive_Message = itemView.findViewById<TextView>(R.id.txt_tittle)
    }
    class SupportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val supportMessage = itemView.findViewById<TextView>(R.id.support_txt_tittle)
    }
}