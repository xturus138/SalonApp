package com.example.comfestsea16.fragment.booking

import Booking
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.comfestsea16.R
import com.example.comfestsea16.activity.RateActivity

class BookingAdapter(private val bookings: MutableList<Booking>) : RecyclerView.Adapter<BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_booked_card, parent, false)
        return BookingViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.dateTextView.text = "${booking.date} ${booking.time}"
        holder.servicesTextView.text = booking.service

        holder.rateButton.setOnClickListener {
            Log.d(ContentValues.TAG, "onBindViewHolder: rateButton is Clicked")
            val intent = Intent(holder.itemView.context, RateActivity::class.java)
            intent.putExtra("bookingId", booking.bookingId)
            intent.putExtra("service", booking.service)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = bookings.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateBookings(newBookings: List<Booking>) {
        bookings.clear()
        bookings.addAll(newBookings)
        notifyDataSetChanged()
    }
}
