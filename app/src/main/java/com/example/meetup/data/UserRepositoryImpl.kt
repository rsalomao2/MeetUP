package com.example.meetup.data

import com.example.meetup.domain.UserRepository
import com.example.meetup.domain.model.User
import com.example.meetup.model.FirestoreUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val db: FirebaseFirestore
) : UserRepository {

    override var userListFlow = flowOf<User>()
    override var exceptionFlow = flowOf<Exception>()

    override fun observeUserList() {
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
                    userListFlow = users.asFlow().map { fireStoreUser ->
                        User(fireStoreUser.first, fireStoreUser.email)
                    }
                }
            }
            .addOnFailureListener { exception ->
                exceptionFlow = flowOf(exception)
            }
    }
}
