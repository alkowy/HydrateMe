package com.azmarzly.settings.domain

import com.azmarzly.authentication.domain.AuthenticationRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {

    operator fun invoke(email: String) = authenticationRepository.sendPasswordResetToEmail(email)

}