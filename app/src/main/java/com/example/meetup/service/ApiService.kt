package com.example.meetup.service

import com.example.meetup.model.User
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/users")
    fun fetchAllUsers(): Call<List<User>>
}
