package com.example.meetup.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserComplete(
    val name: String,
    val username: String,
    val email: String,
    val id: String,
    var imageUrl: String,
    val phone: String,
    val website: String,
    val address: Address,
    var imageBitmap: Bitmap?
): Parcelable
