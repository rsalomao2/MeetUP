package com.example.meetup.model

data class User(
    val name: String,
    val username: String,
    val email: String,
    val id: Int,
    var imageUrl: String,
    val phone: String,
    val website: String,
    val address: Address
) {
    fun convertToUserListItem(): UserListItem {

        val userNameText = "Username: $username"
        val emailText = "Email: $email"
        val idText = "ID: $id"
        val phoneText = "Phone Number: $phone"
        val websiteText = "Website: $website"
        return UserListItem(
           name, userNameText, emailText, idText, imageUrl, phoneText, websiteText, address
        )
    }
}
