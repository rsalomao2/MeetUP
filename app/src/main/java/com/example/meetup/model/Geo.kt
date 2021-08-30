package com.example.meetup.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Geo(
    val lat: Double,
    val lng: Double
): Parcelable
