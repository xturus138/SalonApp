package com.example.comfestsea16.fragment.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service(
    val name: String,
    val description: String,
    val photo: Int
) : Parcelable