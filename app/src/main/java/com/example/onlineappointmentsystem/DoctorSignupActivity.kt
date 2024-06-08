package com.example.onlineappointmentsystem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.onlineappointmentsystem.Model.DoctorSignupModel
import com.example.onlineappointmentsystem.Model.SetUserId
import com.example.onlineappointmentsystem.databinding.ActivityDoctorSignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.Random


class DoctorSignupActivity : AppCompatActivity() {
    lateinit var binding: ActivityDoctorSignupBinding
    lateinit var databaseReference: DatabaseReference
    var docId: String = "1"
    private var boolean: Boolean = false
    private var confirmBoolean: Boolean = false
    private var filePath: Uri? = null

    // request code
    private val PICK_IMAGE_REQUEST = 22
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorSignupBinding.inflate(layoutInflater)
        binding.llBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        getDocId()

        binding.imgProfile.setOnClickListener {
            SelectImage()
        }

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
        databaseReference = FirebaseDatabase.getInstance().reference
        binding.signUpText.setOnClickListener {
            if (binding.etFirstName.text.isEmpty()){
                Utils.showToast(this,"Please enter first name")
            }else if (binding.etLastName.text.isEmpty()){
                Utils.showToast(this,"Please enter last name")
            }else if (binding.educationSpinner.text.isEmpty()){
                Utils.showToast(this,"Please select your education")
            }else if (binding.specialitySpinner.text.isEmpty()){
                Utils.showToast(this,"Please select your speciality")
            }else if (binding.etAddress.text.isEmpty()){
                Utils.showToast(this,"Please enter address")
            }else if (binding.etEmailId.text.isEmpty()){
                Utils.showToast(this,"Please enter email id")
            }else if (binding.etMobileNumber.text.isEmpty()){
                Utils.showToast(this,"Please enter mobile no")
            }else if (binding.etMobileNumber.text.length != 10){
                Utils.showToast(this,"Invalid mobile Number")
            }else if (binding.etPassword.text.isEmpty()){
                Utils.showToast(this,"Please enter password")
            }else if (!Utils.isValidEmail(binding.etEmailId.text.toString())) {
                Utils.showToast(this,"Invalid mail id")
            }else if (binding.etPassword.length() < 5 || binding.etPassword.length() > 15){
                Utils.showToast(this,"Please Enter Password between 5 to 15 Digits")
            }else if (binding.etConfirmPassword.text.isEmpty()){
                Utils.showToast(this,"Please Enter Confirm Password")
            }else if (!binding.etConfirmPassword.text.trim().equals(binding.etPassword.text.trim())){
                Utils.showToast(this,"Password Mismatched")
            }else if (filePath == null){
                Utils.showToast(this,"Please select profile picture")
            }else {
                    uploadImage()
            }
            Log.d("sadhna", binding.educationSpinner.text.toString())
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this,DoctorLoginActivity::class.java))
        }
        setContentView(binding.root)
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


/*    private fun uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            // Defining the child of storageReference
            val ref: StorageReference = storageReference
                .child(
                    "images/"
                            + UUID.randomUUID().toString()
                )

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                .addOnSuccessListener(
                    OnSuccessListener<Any?> { // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss()
                        Toast
                            .makeText(
                                this@MainActivity,
                                "Image Uploaded!!",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    })
                .addOnFailureListener(OnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    Toast
                        .makeText(
                            this@MainActivity,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                })
                .addOnProgressListener(
                    OnProgressListener<Any> { taskSnapshot ->

                        // Progress Listener for loading
                        // percentage on the dialog box
                        val progress: Double = (100.0
                                * taskSnapshot.getBytesTransferred()
                                / taskSnapshot.getTotalByteCount())
                        progressDialog.setMessage(
                            "Uploaded "
                                    + progress.toInt() + "%"
                        )
                    })
        }
    }*/

    private fun signup(url: String) {
        try {

        val data =
            DoctorSignupModel(docId.toInt() + 1,
                binding.etFirstName.text.toString(),
                binding.etLastName.text.toString(),
                binding.educationSpinner.text.toString(),
                binding.specialitySpinner.text.toString(),
                binding.etExperience.text.toString(),
                binding.etAddress.text.toString(),
                binding.etEmailId.text.toString(),
                binding.etMobileNumber.text.toString(),
                binding.etPassword.text.toString(),
                url
            )
        val userId = SetUserId(docId.toInt())
        databaseReference.child("DoctorSignup").push().setValue(data)
        databaseReference.child("Chat").push().setValue(userId)
        binding.etFirstName.setText("")
        binding.etLastName.setText("")
        binding.educationSpinner.text = ""
        binding.specialitySpinner.text = ""
        binding.etExperience.setText("")
        binding.etAddress.setText("")
        binding.etEmailId.setText("")
        binding.etMobileNumber.setText("")
        binding.etPassword.setText("")

        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,DoctorLoginActivity::class.java))

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun getDocId() {
        var mTheReference = FirebaseDatabase.getInstance().reference
        var mUsersReference = mTheReference.child("DoctorSignup")

        mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.children.iterator().hasNext()) {
                    for (postSnapshot in dataSnapshot.children) {
                        docId = postSnapshot.child("docId").getValue().toString()
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