package com.example.comfestsea16.fragment.booking

import Booking
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comfestsea16.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingAdapter

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        recyclerView =
            view.findViewById(R.id.recyclerView)
        adapter = BookingAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val db = FirebaseFirestore.getInstance()
        val userId = getCurrentUserId()

        db.collection("user").document(userId).collection("bookings")
            .get()
            .addOnSuccessListener { documents ->
                val bookings = documents.toObjects(Booking::class.java)
                GlobalScope.launch(Dispatchers.Main) {
                    adapter.updateBookings(bookings)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting bookings", exception)
            }


        return view
    }
    private fun getCurrentUserId(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        return if (currentUser != null) {
            currentUser.uid
        } else {
            ""
        }
    }



}
