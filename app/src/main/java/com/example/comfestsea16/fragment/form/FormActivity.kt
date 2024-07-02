package com.example.comfestsea16.fragment.form

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.comfestsea16.R
import com.example.comfestsea16.databinding.ActivityFormBinding
import com.example.comfestsea16.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var auth: FirebaseAuth
    private var selectedDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val nameData = binding.fullNameEditText
        val numberData = binding.phoneEditText
        val serviceData = binding.serviceTypeSpinner
        val timeData = binding.availableTimeSlot
        val calendarData = binding.calendarView

        setupUI(nameData, numberData, serviceData, timeData, calendarData)
        fetchAndPopulateUserData(nameData, numberData, serviceData, timeData)

        serviceData.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                fetchAndPopulateUserData(nameData, numberData, serviceData, timeData)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun fetchAndPopulateUserData(nameEditText: EditText, numberEditText: EditText, serviceData: Spinner, timeData: Spinner) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val db = FirebaseFirestore.getInstance()
            db.collection("user").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name")
                        val number = document.getString("number")

                        nameEditText.setText(name)
                        numberEditText.setText(number)
                    } else {
                        Log.d("FormActivity", "No user data found in Firestore.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("FormActivity", "Error getting user data: ", exception)
                }

            val serviceValue = serviceData.selectedItem.toString()
            db.collection("services").whereEqualTo("name", serviceValue).get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.first()
                        val sessionDuration = document.getLong("sessionDuration")?.toInt() ?: 1


                        generateTimeSlots(sessionDuration, timeData, document)
                    } else {
                        Log.d("FormActivity", "Service not found in Firestore.")
                        Toast.makeText(this, "Layanan tidak ditemukan.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("FormActivity", "Error getting service duration: ", exception)
                    Toast.makeText(this, "Terjadi kesalahan saat mengambil data layanan.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.d("FormActivity", "Current user is null.")
        }
    }

    private fun generateTimeSlots(sessionDuration: Int, timeData: Spinner, serviceDocument: DocumentSnapshot) {
        val openTime = serviceDocument.getLong("startTime")?.toInt() ?: 9
        val closeTime = serviceDocument.getLong("endTime")?.toInt() ?: 21
        val timeSlots = mutableListOf<String>()

        var currentHour = openTime
        while (currentHour + sessionDuration <= closeTime) {
            val endTime = currentHour + sessionDuration
            timeSlots.add(String.format("%02d:00 - %02d:00", currentHour, endTime))
            currentHour += sessionDuration
        }

        val timeAdapter = ArrayAdapter(this, R.layout.spinner_item, timeSlots)
        timeData.adapter = timeAdapter
        Log.d("FormActivity", "Generated time slots: $timeSlots")
    }

    private fun setupUI(
        nameData: EditText,
        numberData: EditText,
        serviceData: Spinner,
        timeData: Spinner,
        calendarData: CalendarView
    ) {
        val serviceAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.data_name,
            R.layout.spinner_item
        )
        serviceData.adapter = serviceAdapter

        val timeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.time_options,
            R.layout.spinner_item
        )
        timeData.adapter = timeAdapter

        calendarData.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
        }

        navigateBackButton()
        setupSubmitButton(nameData, numberData, serviceData, timeData)
    }

    private fun navigateBackButton() {
        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupSubmitButton(
        nameData: EditText,
        numberData: EditText,
        serviceData: Spinner,
        timeData: Spinner
    ) {
        binding.submitButton.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                val nameValue = nameData.text.toString().trim()
                val numberValue = numberData.text.toString().trim()
                val serviceValue = serviceData.selectedItem.toString()
                val timeValue = timeData.selectedItem.toString()
                if (selectedDate != null) {
                    val date = selectedDate!!.time
                    val dateString = android.text.format.DateFormat.getDateFormat(applicationContext).format(date)
                    saveBookingToFirestore(userId, nameValue, numberValue, serviceValue, timeValue, dateString)
                    Toast.makeText(this, "Booking successful!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("FormActivity", "Current user is null.")
            }
        }
    }

    private fun saveBookingToFirestore(
        userId: String,
        fullName: String,
        phoneNumber: String,
        service: String,
        time: String,
        date: String
    ) {
        val db = FirebaseFirestore.getInstance()
        val bookingId = System.currentTimeMillis().toString()

        val bookingData = hashMapOf(
            "name" to fullName,
            "number" to phoneNumber,
            "service" to service,
            "time" to time,
            "date" to date,
            "status" to "pending"
        )

        db.collection("user").document(userId).collection("bookings").document(bookingId).set(bookingData)
            .addOnSuccessListener {
                Log.d("FormActivity", "Booking created successfully")
            }
            .addOnFailureListener { exception ->
                Log.d("FormActivity", "Error creating booking: ", exception)
            }
    }
}

