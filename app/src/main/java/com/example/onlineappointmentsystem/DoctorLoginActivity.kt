package com.example.onlineappointmentsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.onlineappointmentsystem.databinding.ActivityDoctorLoginBinding
import com.example.onlineappointmentsystem.databinding.ActivityDoctorSignupBinding
import com.example.onlineappointmentsystem.utils.SharedPrefrence
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DoctorLoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityDoctorLoginBinding
    lateinit var signupEmailList: ArrayList<String>
    lateinit var signupPasswordList: ArrayList<String>
    lateinit var emailId: String
    lateinit var session: SharedPrefrence
    var isRegistered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SharedPrefrence(this)
        signupPasswordList = ArrayList()
        signupEmailList = ArrayList()
        getAllEmployee()
        binding.loginText.setOnClickListener {
            userLogin()
        }
        binding.llBack.setOnClickListener {
            startActivity(Intent(this,SelectRoleActivity::class.java))
        }

    }
    private fun userLogin() {
        if (binding.etEmailId.text.isEmpty()){
            Utils.showToast(this,"Please enter email id")
        }else if (binding.etPassword.text.isEmpty()){
            Utils.showToast(this,"Please enter password")
        }else{
            if (signupEmailList.isNotEmpty()){
                for (i in 0 until signupEmailList.size){
                    if (binding.etEmailId.text.toString() == signupEmailList[i]){
                        isRegistered = true
                        if (binding.etPassword.text.toString() == signupPasswordList[i]){
                            emailId = binding.etEmailId.text.toString()
                            saveLoginDetails()
                            Utils.showToast(this,"Login Successfully")
                            startActivity(Intent(this,DoctorHomeActivity::class.java))
                        }else{
                            Utils.showToast(this,"Password not correct")
                        }
                    }
                }
                if (!isRegistered){
                    Utils.showToast(this,"You are not registered")
                }
            }

        }

    }
    private fun getAllEmployee() {
        var mTheReference = FirebaseDatabase.getInstance().reference
        var mUsersReference = mTheReference.child("DoctorSignup")


        mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.children.iterator().hasNext()) {
                    for (postSnapshot in dataSnapshot.children) {
                        val mailId = postSnapshot.child("mailId").getValue().toString()
                        val password = postSnapshot.child("password").getValue().toString()
                        signupEmailList.add(mailId)
                        signupPasswordList.add(password)
                    }

                } else {
                    // userSignupDataBase()
                    Log.d("sadhna", "success")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun saveLoginDetails(){
        var mTheReference = FirebaseDatabase.getInstance().reference
        var mUsersReference = mTheReference.child("DoctorSignup")
        mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.children.iterator().hasNext()) {
                    for (postSnapshot in dataSnapshot.children) {
                        if (postSnapshot.child("mailId").getValue().toString() == emailId) {
                            session.setBoolean(SharedPrefrence.IS_LOGIN,true)
                            session.setString(SharedPrefrence.LOGINTYPE,"doctor")
                            session.setString(SharedPrefrence.LOGINID, postSnapshot.child("docId").getValue().toString())
                            session.setString(SharedPrefrence.USER_FIRST_NAME, postSnapshot.child("docFirstName").getValue().toString())
                            session.setString(SharedPrefrence.USER_LAST_NAME, postSnapshot.child("docLastName").getValue().toString())
                            session.setString(SharedPrefrence.EMAIL_ID, postSnapshot.child("mailId").getValue().toString())
                            session.setString(SharedPrefrence.MOBILE_NO, postSnapshot.child("mobileNo").getValue().toString())
                            session.setString(SharedPrefrence.PASSWORD, postSnapshot.child("password").getValue().toString())
                            session.setString(SharedPrefrence.EXPERIENCE, postSnapshot.child("exp").getValue().toString())
                            session.setString(SharedPrefrence.SPECIALITY, postSnapshot.child("speciality").getValue().toString())
                            session.setString(SharedPrefrence.EDUCATION, postSnapshot.child("education").getValue().toString())
                            session.setString(SharedPrefrence.ADDRESS, postSnapshot.child("address").getValue().toString())
                           // Utils.showToast(this@DoctorLoginActivity,"Data Saved")
                        }
                    }

                } else {
                    // userSignupDataBase()
                    Log.d("sadhna", "success")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,SelectRoleActivity::class.java))
    }
}