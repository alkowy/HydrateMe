package com.azmarzly.registration.presentation

import androidx.lifecycle.ViewModel
import core.domain.ResourceProvider
import core.model.UserActivityEnum
import core.model.UserActivityState
import core.model.toDescriptionResourceStringId
import core.model.toNameResourceStringId
import core.model.toUserActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ActivityPickScreenViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    private val _userActivitiesState = MutableStateFlow(UserActivityEnum.entries.map {
        UserActivityState(
            userActivity = it.toUserActivity(),
            name = resourceProvider.getString(it.toUserActivity().toNameResourceStringId()),
            description = resourceProvider.getString(it.toUserActivity().toDescriptionResourceStringId()),
        )
    })
    val userActivitiesState = _userActivitiesState.asStateFlow()
}