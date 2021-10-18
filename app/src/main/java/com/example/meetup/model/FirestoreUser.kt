package com.example.meetup.model

data class FirestoreUser(
    val first: String,
    val last: String,
    val email: String,
    val phone: String,
    var cpf: String,
)
