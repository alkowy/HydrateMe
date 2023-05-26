package com.azmarzly.authentication.data

import android.util.Log
import com.azmarzly.authentication.domain.AuthenticationRepository
import com.azmarzly.authentication.domain.UserManager
import com.google.firebase.auth.FirebaseAuth
import core.domain.FirestoreRepository
import core.model.Resource
import core.model.UserDataModel
import core.util.doNothing
import core.util.toFirestoreUserDataModel
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userManager: UserManager,
    private val firestore: FirestoreRepository,
) : AuthenticationRepository {

    override fun registerWithEmailAndPassword(
        email: String,
        password: String,
        userDataModel: UserDataModel
    ) = flow {
        emit(Resource.Loading)
        try {
            val registrationResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userDataModelWithFirestoreId = userDataModel.copy(uid = registrationResult.user!!.uid)
            userManager.saveLoggedInUserToLocalPreferences(userDataModelWithFirestoreId)
            firestore.updateUserInFirestore(userDataModelWithFirestoreId.toFirestoreUserDataModel()).collect()
            if (registrationResult.user != null) {
                emit(Resource.Success(userDataModelWithFirestoreId))
            } else {
                emit(Resource.Error("Registration has failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    override fun loginWithEmailAndPassword(
        email: String,
        password: String,
    ) = channelFlow {
        send(Resource.Loading)
        try {
            val loginResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Log.d("ANANAS", "loginWithEmailAndPassword: loginresult $loginResult")
            firestore.fetchUserFromFirestore(loginResult.user!!.uid)
                .collectLatest { fetchedUser ->
                    Log.d("ANANAS", "loginWithEmailAndPassword: $fetchedUser")
                    when (fetchedUser) {
                        is Resource.Success -> {
                            userManager.saveLoggedInUserToLocalPreferences(fetchedUser.data)
                            userManager.saveUserId(loginResult.user!!.uid)
                            send(Resource.Success(fetchedUser.data))
                        }

                        is Resource.Error -> {
                            send(Resource.Error<UserDataModel>(errorMessage = fetchedUser.errorMessage))
                        }

                        Resource.Loading -> send(Resource.Loading)
                        Resource.EmptyState -> doNothing()
                    }
                }
        } catch (e: Exception) {
            send(Resource.Error(errorMessage = e.message))
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
        userManager.clearUserDataInLocalPreferences()
    }
}