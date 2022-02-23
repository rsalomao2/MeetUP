package com.example.meetup.presentation.signin.social

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class SignInViewModelFactory(
    private val firebaseInstance: FirebaseAuth,
    val fbLoginManager: LoginManager,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(firebaseInstance, fbLoginManager, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}