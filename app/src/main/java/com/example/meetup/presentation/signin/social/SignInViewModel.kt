package com.example.meetup.presentation.signin.social

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.login.LoginManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel(
    val firebase: FirebaseAuth,
    var fbLoginManager: LoginManager,
    application: Application
) : AndroidViewModel(application) {

    private val _anonimouslyTask = MutableLiveData<Boolean>()
    val anonimouslyTask: LiveData<Boolean> get() = _anonimouslyTask

    private val _firebaseLoginTask = MutableLiveData<Boolean>()
    val firebaseLoginTask: LiveData<Boolean> get() = _firebaseLoginTask

    init {
        fbLoginManager = LoginManager.getInstance()
    }

    //    private fun setupFacebook() {
//        fbLoginManager
//            .logInWithReadPermissions(, listOf("public_profile", "email"))
//        LoginManager.getInstance().registerCallback(callbackManager,
//            object : FacebookCallback<LoginResult> {
//                override fun onSuccess(loginResult: LoginResult) {
//                    val token = loginResult.accessToken.token
//                    val credential = FacebookAuthProvider.getCredential(token)
//                    signInOnFirebase(credential)
//                    Log.d("MainActivity", "Facebook token: " + loginResult.accessToken.token)
//                }
//
//                override fun onCancel() {
//                    Log.d("MainActivity", "Facebook onCancel.")
//                }
//
//                override fun onError(error: FacebookException) {
//                    Log.d("MainActivity", "Facebook onError.")
//                }
//            })
//    }

    fun signInOnFirebase(credential: AuthCredential) {
        firebase.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _firebaseLoginTask.value = true
                } else {
                    Log.d("###", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun signInAnonymously() {
        firebase.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _anonimouslyTask.value = true
                } else {
                    Log.d("###", task.exception?.localizedMessage ?: "Error ao login anonnymously")
                }
            }
    }

}