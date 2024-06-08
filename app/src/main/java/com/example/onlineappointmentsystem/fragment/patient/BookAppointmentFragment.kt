package com.example.onlineappointmentsystem.fragment.patient

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import com.example.onlineappointmentsystem.Model.BookAppointmentModel
import com.example.onlineappointmentsystem.Model.PatientSignupModel
import com.example.onlineappointmentsystem.R
import com.example.onlineappointmentsystem.Utils
import com.example.onlineappointmentsystem.databinding.FragmentBookAppointmentBinding
import com.example.onlineappointmentsystem.utils.SharedPrefrence
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookAppointmentFragment : Fragment() {
    lateinit var binding: FragmentBookAppointmentBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var appId: String
    var docList = arrayListOf<String>()
    lateinit var session: SharedPrefrence

    private val checkInTimePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> // logic to properly handle
            // the picked timings by user
            val formattedTime: String = when {
                hourOfDay == 0 -> {
                    if (minute < 10) {
                        "${hourOfDay + 12}:0${minute} AM"
                    } else {
                        "${hourOfDay + 12}:${minute} AM"
                    }
                }
                hourOfDay > 12 -> {
                    if (minute < 10) {
                        "${hourOfDay - 12}:0${minute} PM"
                    } else {
                        "${hourOfDay - 12}:${minute} PM"
                    }
                }
                hourOfDay == 12 -> {
                    if (minute < 10) {
                        "${hourOfDay}:0${minute} PM"
                    } else {
                        "${hourOfDay}:${minute} PM"
                    }
                }
                else -> {
                    if (minute < 10) {
                        "${hourOfDay}:${minute} AM"
                    } else {
                        "${hourOfDay}:${minute} AM"
                    }
                }
            }

            binding.txtTime.text = formattedTime
            //  selectedCheckIn = formattedTime
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookAppointmentBinding.inflate(inflater,container,false)
        databaseReference = FirebaseDatabase.getInstance().reference
        session = SharedPrefrence(requireContext())
        getAppointmentId()
        getDoctorList()
        binding.signUpText.setOnClickListener {
            if (binding.diseaseSpinner.text.isEmpty()){
                Utils.showToast(requireContext(),"Please Enter your disease name")
            }else if (binding.txtSelectDate.text.isEmpty()){
                Utils.showToast(requireContext(),"Please Select your appointment date")
            }else if (binding.txtTime.text.isEmpty()){
                Utils.showToast(requireContext(),"Please Select your appointment time")
            }else{
                bookAppointment()
            }
        }

        binding.txtSelectDate.setOnClickListener {
            openDateCalender(requireContext(),binding.txtSelectDate)
        }

        binding.txtTime.setOnClickListener {
            val timePicker = TimePickerDialog(requireContext(),R.style.DialogTheme, checkInTimePickerDialogListener,9, 30, false)
            timePicker.show()
        }
      //  binding.doctorSpinner.setSelection(1)
        Log.d("sadhna",docList.size.toString())
        val gender = ArrayList<String>()
      for (i in 0 until docList.size){
          gender.add(docList[i])
      }
        Log.d("sadhna",gender.size.toString())

        val genderAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), R.layout.drop_down_menu_items, docList)
        genderAdapter.setDropDownViewResource(R.layout.drop_down_menu_items)
        binding.doctorSpinner.adapter = genderAdapter
        binding.doctorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.doctorSpinner.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        return binding.root
    }

    private fun getAppointmentId() {
        var mTheReference = FirebaseDatabase.getInstance().reference
        var mUsersReference = mTheReference.child("BookAppointment")

        mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.children.iterator().hasNext()) {
                    for (postSnapshot in dataSnapshot.children) {
                        appId = postSnapshot.child("appId").getValue().toString()
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

    private fun bookAppointment() {
        val data = BookAppointmentModel(appId .toInt() + 1,session.getString(SharedPrefrence.USER_FIRST_NAME) + " " +session.getString(SharedPrefrence.USER_LAST_NAME),binding.diseaseSpinner.text.toString(),"Akanksha",binding.txtSelectDate.text.toString(),binding.txtTime.text.toString())
        databaseReference.child("BookAppointment").push().setValue(data)
        Utils.showToast(requireContext(),"Appointment Book Successfully")
        getAppointmentId()
        binding.diseaseSpinner.setText("")
        binding.txtSelectDate.setText("")
        binding.txtTime.setText("")

    }

    private fun openDateCalender(context: Context, textView: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(context,R.style.DialogTheme, DatePickerDialog.OnDateSetListener {
                view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)

            textView.text = formattedDate
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = calendar.timeInMillis;
        datePickerDialog.show()
    }

    fun getDoctorList(){
        var mTheReference = FirebaseDatabase.getInstance().reference
        var mUsersReference = mTheReference.child("DoctorSignup")
        mUsersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.children.iterator().hasNext()) {
                    for (postSnapshot in dataSnapshot.children) {
                           val firstName = postSnapshot.child("docFirstName").getValue().toString()
                           val lastName = postSnapshot.child("docLastName").getValue().toString()
                        docList.add("$firstName $lastName")
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
}