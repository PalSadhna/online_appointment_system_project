package com.example.onlineappointmentsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.onlineappointmentsystem.Model.ChatListResposne
import com.example.onlineappointmentsystem.Model.PatientSignupModel
import com.example.onlineappointmentsystem.Model.SetUserId
import com.example.onlineappointmentsystem.adapter.DoctorChatAdapter
import com.example.onlineappointmentsystem.databinding.ActivityOneToOneChatBinding
import com.example.onlineappointmentsystem.utils.SharedPrefrence
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OneToOneChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityOneToOneChatBinding
    lateinit var databaseReference: DatabaseReference
    var senderId = ""
    var senderName = ""
    var receiverName = ""
    var receiverId = ""
    lateinit var session: SharedPrefrence
    val arrayList = ArrayList<ChatListResposne>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOneToOneChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SharedPrefrence(this)
        senderName = session.getString(SharedPrefrence.USER_FIRST_NAME) + session.getString(SharedPrefrence.USER_LAST_NAME)
        senderId =  session.getString(SharedPrefrence.LOGINID).toString()
        receiverId = (intent.getStringExtra("receiverId")).toString()
        receiverName = intent.getStringExtra("receiverName").toString()
        val PImage = intent.getStringExtra("pImage").toString()
        Glide.with(this).load(PImage).placeholder(R.drawable.chat_profile_pic).error(R.drawable.chat_profile_pic).into(binding.adminImage)
        binding.adminName.text = receiverName
        databaseReference = FirebaseDatabase.getInstance().reference
        getChatList()
        binding.chatRecycler.layoutManager = LinearLayoutManager(this)
binding.sentBtn.setOnClickListener {
    sendChat()
}
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


    }

    fun sendChat(){
            val senderData = MsgSendModel(
                senderId,
               senderName,
            "2:30Pm",
                "20/04/2024",
                receiverId,
                receiverName,
                binding.messageBox.text.toString(),
                "sender"
            )
            val receiverData = MsgSendModel(
                senderId,
                senderName,
                "2:30Pm",
                "20/04/2024",
                receiverId,
                receiverName,
                binding.messageBox.text.toString(),
                "receiver"
            )
            println("userId $senderId $receiverId")
            databaseReference.child("Chat").child(senderId).push().setValue(senderData)
            databaseReference.child("Chat").child(receiverId).push().setValue(receiverData)
            binding.messageBox.setText("")
        getChatList()
            Toast.makeText(this, "Sended", Toast.LENGTH_SHORT).show()

    }

    private fun getChatList() {
        var mTheReference = FirebaseDatabase.getInstance().reference
        var mUsersReference = mTheReference.child("Chat").child(receiverId)

        mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.children.iterator().hasNext()) {
                    arrayList.clear()
                    for (postSnapshot in dataSnapshot.children) {
                        val chatMsg = postSnapshot.child("msg").getValue().toString()
                        val msgType = postSnapshot.child("msgType").getValue().toString()
                        arrayList.add(ChatListResposne(chatMsg,msgType))
                    }
                    binding.chatRecycler.adapter = DoctorChatAdapter(this@OneToOneChatActivity,arrayList,senderId)

                } else {
                    // userSignupDataBase()
                    Log.d("userSignup", "success")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}