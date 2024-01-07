package com.azmarzly.profile.presentation

import core.model.UserActivityEnum

data class ProfileUiState(
    val profileImageUrl: String,
    val useLocalImageFromUri: Boolean,
    val name: String,
    val email: String,
    val age: String,
    val weight: String,
    val height: String,
    val activity: String,
    val dailyGoal: String,
)