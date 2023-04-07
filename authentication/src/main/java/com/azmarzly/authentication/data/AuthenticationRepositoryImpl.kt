package com.azmarzly.authentication.data

import com.azmarzly.authentication.domain.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import core.model.LoggedInUser
import core.model.Resource
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthenticationRepository {

    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<LoggedInUser> {
        var result: Resource<LoggedInUser> = Resource.Error()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                result = if (task.isSuccessful) {
                    Resource.Success(
                        LoggedInUser(
                            uId = task.result.user?.uid!!,
                            email = task.result.user?.email!!
                        )
                    )
                } else {
                    Resource.Error(errorMessage = task.exception?.message)
                }
            }
        return result
    }
}