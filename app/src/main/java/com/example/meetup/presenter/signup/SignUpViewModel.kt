package com.example.meetup.presenter.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetup.R
import com.example.meetup.util.CpfValidator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpViewModel: ViewModel() {

    private var firebaseAuth: FirebaseAuth = Firebase.auth

    private val _isValidPassword = MutableLiveData<Boolean>()
    val isValidPassword: LiveData<Boolean> get() = _isValidPassword

    private val _passwordErrorMessage = MutableLiveData<String>()
    val passwordErrorMessage: LiveData<String> get() = _passwordErrorMessage

    private val _isValidEmail = MutableLiveData<Boolean>()
    val isValidEmail: LiveData<Boolean> get() = _isValidEmail

    private val _emailErrorMessage = MutableLiveData<String>()
    val emailErrorMessage: LiveData<String> get() = _emailErrorMessage

    private val _isValidFirstName = MutableLiveData<Boolean>()
    val isValidFirstName: LiveData<Boolean> get() = _isValidFirstName

    private val _firstNameErrorMessage = MutableLiveData<String>()
    val firstNameErrorMessage: LiveData<String> get() = _firstNameErrorMessage

    private val _isValidLastName = MutableLiveData<Boolean>()
    val isValidLastName: LiveData<Boolean> get() = _isValidLastName

    private val _lastNameErrorMessage = MutableLiveData<String>()
    val lastNameErrorMessage: LiveData<String> get() = _lastNameErrorMessage

    private val _isValidPhoneNumber = MutableLiveData<Boolean>()
    val isValidPhoneNumber: LiveData<Boolean> get() = _isValidPhoneNumber

    private val _phoneNumberErrorMessage = MutableLiveData<String>()
    val phoneNumberErrorMessage: LiveData<String> get() = _phoneNumberErrorMessage

    private val _isValidCpf = MutableLiveData<Boolean>()
    val isValidCpf: LiveData<Boolean> get() = _isValidCpf

    private val _cpfErrorMessage = MutableLiveData<String>()
    val cpfErrorMessage: LiveData<String> get() = _cpfErrorMessage

    fun createFirebaseUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("###", "createUserWithEmail:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("###", "createUserWithEmail:failure", task.exception)

                }
            }
    }

    fun saveUserToDb(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        cpf: String
    ) {
        val db = Firebase.firestore
        val user = hashMapOf(
            "first" to firstName,
            "last" to lastName,
            "email" to email,
            "phone" to phoneNumber,
            "cpf" to cpf
        )
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("SignUpFragment", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("SignUpFragment", "Error adding document", e)
            }
    }

    fun isValidFirstName(firstName: String): Boolean {
        return if (firstName.isEmpty()) {
            _isValidFirstName.value = false
            _firstNameErrorMessage.value = "Field cannot be empty"
            false
        } else {
            _isValidFirstName.value = true
            true
        }
    }

    fun isValidLastName(lastName: String): Boolean {
        return if (lastName.isEmpty()) {
            _lastNameErrorMessage.value = "Field Cannot be empty"
            _isValidLastName.value = false
            false
        } else {
            _isValidLastName.value = true
            true
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

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneRegex = "(\\(\\d{2}\\))(\\d{4,5}-\\d{4})"
        return when {
            phoneNumber.isEmpty() -> {
                _phoneNumberErrorMessage.value = "Field cannot be empty"
                _isValidPhoneNumber.value = false
                false
            }
            !phoneNumber.matches(phoneRegex.toRegex()) -> {
                _phoneNumberErrorMessage.value = "Invalid phone number"
                _isValidPhoneNumber.value = false
                false
            }
            else -> {
                _isValidPhoneNumber.value = true
                true
            }
        }
    }

    fun isValidCpf(cpf: String): Boolean {
        return when {
            cpf.isEmpty() -> {
                _isValidCpf.value = false
                _cpfErrorMessage.value = "Field cannot be empty"
                false
            }
            !CpfValidator.myValidateCPF(cpf) -> {
                _isValidCpf.value = false
                _cpfErrorMessage.value = "CPF is invalid"
                false
            }
            else -> {
                _isValidCpf.value = true
                true
            }
        }
    }
}