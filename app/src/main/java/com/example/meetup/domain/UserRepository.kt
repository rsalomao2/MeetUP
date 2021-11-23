package com.example.meetup.domain

import com.example.meetup.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    var userListFlow: Flow<User>
    var exceptionFlow: Flow<Exception>
    fun observeUserList()
}

