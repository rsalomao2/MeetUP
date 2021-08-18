package com.example.meetup.extensions

import android.content.Context
import android.widget.Toast

fun Context.showToast(msg: String, loop: Int = 1){
	repeat(loop){
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
	}
}
