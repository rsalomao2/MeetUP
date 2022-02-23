package com.example.meetup.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashViewModel : ViewModel() {

    private val _mAuth = MutableLiveData<FirebaseAuth>()
    val mAuth: LiveData<FirebaseAuth> get() = _mAuth

    init {
        _mAuth.value = Firebase.auth
    }
}
