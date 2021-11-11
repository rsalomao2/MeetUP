package com.example.meetup.presentation.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UsersListViewModel : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    fun showLoading(){
        _loadingState.value = true
    }

    fun hideLoading(){
        _loadingState.value = false
    }

    fun randomUrl(): String {
        val listOfUrl = listOf(
            "https://picsum.photos/130?random=$1.jpg",
            "https://picsum.photos/200?random=2.jpg",
            "https://picsum.photos/200?random=3.jpg",
            "https://picsum.photos/200?random=4.jpg",
            "https://picsum.photos/200?random=5.jpg",
            "https://picsum.photos/200?random=6.jpg",
            "https://picsum.photos/200?random=7.jpg",
            "https://picsum.photos/200?random=8.jpg"
        )
        return listOfUrl.random()
    }
}
