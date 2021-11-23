package com.example.meetup.presenter.userlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetup.model.FirestoreUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UsersListViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _connectionFailureDialogLiveData = MutableLiveData<Boolean>()
    val connectionFailureDialogLiveData: LiveData<Boolean> get() = _connectionFailureDialogLiveData

    private val _userListLivedata = MutableLiveData<List<FirestoreUser>>()
    val userListLivedata: LiveData<List<FirestoreUser>> get() = _userListLivedata

    fun setupFirebaseFirestore() {
        _isLoading.value = true
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                result.let {
                    val users = ArrayList<FirestoreUser>()
                    for (document in result) {
                        val user = document.toObject<FirestoreUser>()
                        user.let {
                            user.apply {
                                id = document.id
                            }
                            users.add(user)
                        }
                    }
                    _userListLivedata.value = users
                    _isLoading.value = false
                }
            }
            .addOnFailureListener { exception ->
                _connectionFailureDialogLiveData.value = true
                Log.w("###", "Error getting documents.", exception)
                _isLoading.value = false
            }
    }

}
