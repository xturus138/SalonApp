package com.example.comfestsea16.fragment.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service(
    var id: String? = null, // Allow null for the id since it's assigned later
    val name: String? = null, // Allow null for these fields initially
    val description: String? = null,
    val imageUrl: String? = null
) : Parcelable