package com.azmarzly.authentication.domain

import core.model.LoggedInUser
import core.model.Resource

interface AuthenticationRepository {

    fun loginWithEmailAndPassword(email: String, password: String): Resource<LoggedInUser>
}