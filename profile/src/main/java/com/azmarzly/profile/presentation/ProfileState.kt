package com.azmarzly.profile.presentation

import core.model.UserActivityEnum

data class ProfileUiState(
    val profileImageUrl: String,
    val name: String,
    val email: String,
    val age: String,
    val weight: String,
    val height: String,
    val activity: UserActivityEnum,
    val dailyGoal: String,
)