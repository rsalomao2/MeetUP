package com.example.meetup.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FirestoreUser(
    val first: String,
    val last: String,
    val email: String,
    val phone: String,
    var cpf: String,
): Parcelable
