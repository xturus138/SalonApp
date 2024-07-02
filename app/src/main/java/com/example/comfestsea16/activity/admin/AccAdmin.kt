package com.example.comfestsea16.activity.admin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comfestsea16.databinding.ActivityAccAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AccAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityAccAdminBinding
    private lateinit var adapter: AdminBookingAdapter
    private val bookingsList = mutableListOf<BookingAdmin>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewBookings.layoutManager = LinearLayoutManager(this)
        adapter = AdminBookingAdapter(bookingsList)
        binding.recyclerViewBookings.adapter = adapter

        fetchBookingsData()

        binding.saveButton.setOnClickListener {
            updateBookingStatuses { success ->
                if (success) {
                    refreshBookingsData()
                }
            }
        }
    }

    private fun updateBookingStatuses(onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val bookingsToUpdate = adapter.getBookingsWithChangedStatus()

        var updatesCompleted = 0
        for (booking in bookingsToUpdate) {
            val bookingRef = db.collection("user")
                .document(booking.userId)
                .collection("bookings")
                .document(booking.bookingId)

            bookingRef.update("status", "booked")
                .addOnSuccessListener {
                    updatesCompleted++
                    if (updatesCompleted == bookingsToUpdate.size) {
                        onComplete(true)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error updating booking status: ", exception)
                    onComplete(false)
                }
        }
    }

    private fun fetchBookingsData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("user")
            .get()
            .addOnSuccessListener { userDocuments ->
                for (userDocument in userDocuments) {
                    userDocument.reference.collection("bookings")
                        .whereEqualTo("status", "pending")
                        .get()
                        .addOnSuccessListener { bookingDocuments ->
                            for (bookingDocument in bookingDocuments) {
                                val booking = bookingDocument.toObject(BookingAdmin::class.java)
                                booking.bookingId = bookingDocument.id
                                booking.userId = userDocument.id
                                bookingsList.add(booking)
                            }
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("FirestoreError", "Error fetching bookings: ", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error fetching users: ", exception)
            }
    }

    private fun refreshBookingsData() {
        bookingsList.clear()
        adapter.notifyDataSetChanged()
        fetchBookingsData()
    }
}
