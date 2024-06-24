package com.example.comfestsea16.fragment.booking

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.comfestsea16.R

class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dateTextView: TextView = itemView.findViewById(R.id.appointmentDate)
    val servicesTextView: TextView = itemView.findViewById(R.id.appointmentServices)
    val rateButton: Button = itemView.findViewById(R.id.rateButton)
}
