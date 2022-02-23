package com.example.meetup.presentation.signin.email

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class EmailLoginViewModel : ViewModel() {

    private var auth: FirebaseAuth? = null

    private val _firebaseEmailLoginTask = MutableLiveData<Boolean>()
    val firebaseEmailLoginTask: LiveData<Boolean> get() = _firebaseEmailLoginTask

    private val _isValidEmail = MutableLiveData<Boolean>()
    val isValidEmail: LiveData<Boolean> get() = _isValidEmail

    private val _emailErrorMessage = MutableLiveData<String>()
    val emailErrorMessage: LiveData<String> get() = _emailErrorMessage

    private val _isValidPassword = MutableLiveData<Boolean>()
    val isValidPassword: LiveData<Boolean> get() = _isValidPassword

    private val _passwordErrorMessage = MutableLiveData<String>()
    val passwordErrorMessage: LiveData<String> get() = _passwordErrorMessage



    init {
        auth = FirebaseAuth.getInstance()
    }

    fun signInWithFirebase(email: String, password: String) {
        auth?.let { login ->
            login.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _firebaseEmailLoginTask.value = true
                    } else {
                        Log.w("###", "signInWithEmail:failure", task.exception)
                    }
                }
        }
    }

    fun isValidEmail(email: String): Boolean {

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        return when {
            email.isEmpty() -> {
                _emailErrorMessage.value = "Field Cannot Be Empty"
                _isValidEmail.value = false
                false
            }
            !email.matches(emailPattern.toRegex()) -> {
                _emailErrorMessage.value = "Invalid Email Adress"
                _isValidEmail.value = false
                false
            }
            else -> {
                _isValidEmail.value = true
                true
            }
        }
    }

     fun isValidPassword(password: String): Boolean {

        val passwordValidationRegex = "^" + "(?=.*[0-9])" + //at least 1 digit
                "(?=.*[a-zA-Z])" + //any letter
                ".{6,8}" +  // between 6 and 8 digits
                "$"

        return when {
            password.isEmpty() -> {
                _passwordErrorMessage.value = "Field Cannot Be Empty"
                _isValidPassword.value = false
                false
            }
            password.length < 3 -> {
                _passwordErrorMessage.value = "Password is too short"
                _isValidPassword.value = false
                false
            }
            password.length > 8 -> {
                _passwordErrorMessage.value = "Password is too long"
                _isValidPassword.value = false
                false
            }
            !password.matches(passwordValidationRegex.toRegex()) -> {
                _passwordErrorMessage.value = "Password must have at least one digit(0–9) and one letter(a-Z)"
                _isValidPassword.value = false
                false
            }
            else -> {
                _isValidPassword.value = true
                true
            }
        }
    }

}