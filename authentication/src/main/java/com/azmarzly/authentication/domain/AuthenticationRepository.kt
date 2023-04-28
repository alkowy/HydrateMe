package com.azmarzly.authentication.domain

import core.model.Resource
import core.model.UserDataModel
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun loginWithEmailAndPassword(email: String, password: String): Flow<Resource<UserDataModel>>
    fun signOut()
}