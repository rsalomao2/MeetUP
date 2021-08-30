package com.example.meetup.model

data class UserListItem(
    val name: String,
    val userNameText: String,
    val emailText: String,
    val idText: String,
    var imageUrl: String = "",
    val phoneText: String,
    val websiteText: String,
    val address: Address
)
