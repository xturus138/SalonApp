package com.example.comfestsea16.fragment.booking

import Booking
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comfestsea16.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingAdapter
    private lateinit var allBookings: List<Booking>

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = BookingAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val db = FirebaseFirestore.getInstance()
        val userId = getCurrentUserId()

        db.collection("user").document(userId).collection("bookings")
            .get()
            .addOnSuccessListener { documents ->
                allBookings = documents.toObjects(Booking::class.java)
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.updateBookings(allBookings)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting bookings", exception)
            }

        val upcomingButton = view.findViewById<Button>(R.id.button_upcoming)
        val historyButton = view.findViewById<Button>(R.id.button_history)

        upcomingButton.setOnClickListener {
            filterBookings(true)
            updateButtonColors(upcomingButton, historyButton)
        }

        historyButton.setOnClickListener {
            filterBookings(false)
            updateButtonColors(historyButton, upcomingButton)
        }

        return view
    }

    private fun filterBookings(showUpcoming: Boolean) {
        val filteredBookings = if (showUpcoming) {
            allBookings
        } else {
            allBookings
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.updateBookings(filteredBookings)
        }
    }

    private fun updateButtonColors(activeButton: Button, inactiveButton: Button) {
        val activeColor = ContextCompat.getColor(requireContext(), R.color.my_primary)
        val inactiveColor = ContextCompat.getColor(requireContext(), R.color.grayblack)

        activeButton.setBackgroundColor(activeColor)
        activeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        inactiveButton.setBackgroundColor(inactiveColor)
        inactiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.my_primary))
    }

    private fun getCurrentUserId(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        return currentUser?.uid ?: ""
    }
}
