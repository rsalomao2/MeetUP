package com.example.meetup

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class MyApplication: Application() {
	override fun onCreate() {
		super.onCreate()
		FacebookSdk.sdkInitialize(this)
		AppEventsLogger.activateApp(this)
	}
}
