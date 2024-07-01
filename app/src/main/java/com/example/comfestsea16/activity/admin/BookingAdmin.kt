package com.example.comfestsea16.activity.admin

data class BookingAdmin(
    var bookingId: String = "",
    var userId: String = "",
    val date: String = "",
    val name: String = "",
    val number: String = "",
    val service: String = "",
    val status: String = "",
    val time: String = "",
    var isChecked: Boolean = false
)
