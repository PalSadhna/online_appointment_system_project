package com.example.onlineappointmentsystem

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.onlineappointmentsystem.Model.PatientSignupModel
import com.example.onlineappointmentsystem.Model.SetUserId
import com.example.onlineappointmentsystem.databinding.ActivityPatientSignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.util.Random

class PatientSignupActivity : AppCompatActivity() {
    lateinit var binding: ActivityPatientSignupBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var pId: String
    private var boolean: Boolean = false
    private var confirmBoolean: Boolean = false
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 22

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference
        getPid()
        binding.imgProfile.setOnClickListener {
            SelectImage()
        }
        binding.loginText.setOnClickListener {
            startActivity(Intent(this,PatientLoginActivity::class.java))
        }
        binding.llBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        getPid()
        binding.eyeEnableDisable.setOnClickListener {
            if (boolean) {
                boolean = false
                binding.eyeEnableDisable.setImageResource(R.drawable.view)
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.etPassword.setSelection(binding.etPassword.length())
            } else {
                boolean = true
                binding.eyeEnableDisable.setImageResource(R.drawable.hide)
                binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.etPassword.setSelection(binding.etPassword.length())
            }
        }
        binding.eyeConfirmEnableDisable.setOnClickListener {
            if (confirmBoolean) {
                confirmBoolean = false
                binding.eyeConfirmEnableDisable.setImageResource(R.drawable.view)
                binding.etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
            } else {
                confirmBoolean = true
                binding.eyeConfirmEnableDisable.setImageResource(R.drawable.hide)
                binding.etConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
            }
        }
        binding.llSignUp.setOnClickListener {
            if (binding.etFirstName.text.isEmpty()) {
                Utils.showToast(this, "Please Enter first name")
            } else if (binding.etLastName.text.isEmpty()) {
                Utils.showToast(this, "Please enter last name")
            } else if (binding.etEmailId.text.isEmpty()) {
                Utils.showToast(this, "Please Enter email id")
            } else if (binding.etMobileNumber.text.isEmpty()) {
                Utils.showToast(this, "Please Enter mobile Number")
            } else if (binding.etPassword.text.isEmpty()) {
                Utils.showToast(this, "Please Enter Password")
            } else if(binding.etMobileNumber.text.length != 10){
                Utils.showToast(this, "Invalid Mobile Number")
            }else if (!Utils.isValidEmail(binding.etEmailId.text.toString())){
                Utils.showToast(this, "Invalid Mail Id")
            }else if (binding.etPassword.length() < 5 || binding.etPassword.length() > 15){
                Utils.showToast(this,"Please Enter Password between 5 to 15 Digits")
            }else if (binding.etConfirmPassword.text.isEmpty()){
                Utils.showToast(this,"Please Enter Confirm Password")
            }else if (binding.etPassword.text.isEmpty()){
                Utils.showToast(this,"Please Enter Confirm Password")
            }else if (!binding.etConfirmPassword.text.trim().equals(binding.etPassword.text.trim())){
                Utils.showToast(this,"Password Mismatched")
            }else {
                uploadImage()
            }

        }

        binding.llBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun signup(url: String) {
        try {
            val data = PatientSignupModel(
                pId.toInt() + 1,
                binding.etFirstName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etEmailId.text.toString(),
                binding.etMobileNumber.text.toString(),
                binding.etPassword.text.toString(),
                url
            )
            databaseReference.child("PatientSignup").push().setValue(data)
            val userId = SetUserId(pId.toInt())
            databaseReference.child("Chat").push().setValue(userId)

            binding.etFirstName.setText("")
            binding.etLastName.setText("")
            binding.etMobileNumber.setText("")
            binding.etEmailId.setText("")
            binding.etPassword.setText("")
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,PatientLoginActivity::class.java))
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun getPid() {
        var mTheReference = FirebaseDatabase.getInstance().reference
        var mUsersReference = mTheReference.child("PatientSignup")

        mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.children.iterator().hasNext()) {
                    for (postSnapshot in dataSnapshot.children) {
                        pId = postSnapshot.child("pid").getValue().toString()
                    }

                } else {
                    // userSignupDataBase()
                    Log.d("userSignup", "success")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun SelectImage() {

        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filePath
                    )
                binding.imgProfile.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }

    fun uploadImage() {
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReference("Images/" + Random().nextInt(50))
        storageReference.putFile(filePath!!).addOnSuccessListener { taskSnapshot ->
            // Retrieve the download URL
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                signup(downloadUrl)
            }.addOnFailureListener { exception ->
                // Handle any errors
                Log.e("Firebase", "Error getting download URL", exception)
            }
        }.addOnFailureListener { exception ->
            // Handle any errors
            Log.e("Firebase", "File upload failed", exception)
        }
    }
}