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
import com.example.comfestsea16.databinding.FragmentSecondBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {
    private lateinit var binding: FragmentSecondBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingAdapter
    private lateinit var allBookings: List<Booking>

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = BookingAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val db = FirebaseFirestore.getInstance()
        val userId = getCurrentUserId()
        val progress = binding.progressBar

        progress.visibility = View.VISIBLE

        db.collection("user").document(userId).collection("bookings")
            .get()
            .addOnSuccessListener { documents ->
                allBookings = documents.toObjects(Booking::class.java)
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.updateBookings(allBookings)
                    filterBookings(true)
                }
                progress.visibility = View.GONE
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
        updateButtonColors(upcomingButton, historyButton)
        return view
    }

    private fun filterBookings(showUpcoming: Boolean) {
        if (::allBookings.isInitialized) {
            val filteredBookings = if (showUpcoming) {
                allBookings.filter { it.status == "pending" }
            } else {
                allBookings.filter { it.status == "booked" }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.updateBookings(filteredBookings)
            }
        } else {
            Log.e(TAG, "Error getting filtered Bookings")
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
