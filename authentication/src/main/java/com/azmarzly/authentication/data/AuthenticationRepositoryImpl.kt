package com.azmarzly.authentication.data

import com.azmarzly.authentication.domain.AuthenticationRepository
import com.azmarzly.authentication.domain.UserManager
import com.google.firebase.auth.FirebaseAuth
import core.domain.FirestoreRepository
import core.model.Resource
import core.model.UserActivity
import core.model.UserDataModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userManager: UserManager,
    private val firestore: FirestoreRepository,
) : AuthenticationRepository {

    override fun loginWithEmailAndPassword(email: String, password: String) = flow {
        emit(Resource.Loading)
        try {
            firebaseAuth.currentUser
            val loginResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = UserDataModel(
                uId = loginResult.user?.uid!!,
                email = loginResult.user?.email!!,
                birthDate = LocalDateTime.now(),
                gender = "male",
                userActivity = UserActivity.AverageActivity(),
                height = 200.0,
                name = "Name",
                weight = 100.0

            )
            userManager.saveLoggedInUserToLocalPreferences(user)
            firestore.testfun(user)
            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Error(errorMessage = e.message))
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
        userManager.clearUserData()
    }
}