package com.example.comfestsea16.activity.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.comfestsea16.databinding.ItemBookingBinding

class AdminBookingAdapter(private val bookings: MutableList<BookingAdmin>) : RecyclerView.Adapter<AdminBookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(val binding: ItemBookingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.binding.appointmentDate.text = "${booking.date}, ${booking.time}"
        holder.binding.appointmentName.text = booking.name
        holder.binding.appointmentServices.text = booking.service
        holder.binding.bookingStatus.isChecked = booking.isChecked
        holder.binding.bookingStatus.setOnCheckedChangeListener { _, isChecked ->
            booking.isChecked = isChecked
        }
    }

    override fun getItemCount(): Int = bookings.size

    // Method to get bookings with changed statuses
    fun getBookingsWithChangedStatus(): List<BookingAdmin> {
        return bookings.filter { it.isChecked && it.status != "booked" }
    }
}
