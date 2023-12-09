package com.azmarzly.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.core.R
import com.azmarzly.home.components.HomeScreenBottomBarItem
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.model.Resource
import core.model.UserDataModel
import core.util.doNothing
import core.util.toFirestoreUserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenParentViewModel @Inject constructor(
    private val firestoreUserUseCase: UpdateFirestoreUserUseCase,
) : ViewModel() {

    companion object {
        val homeScreens = listOf(
            HomeScreenBottomBarItem(description = "Home", iconSelected = R.drawable.ic_home_selected, iconUnselected = R.drawable.ic_home_unselected),
            HomeScreenBottomBarItem(
                description = "Calendar",
                iconSelected = R.drawable.ic_calendar_selected,
                iconUnselected = R.drawable.ic_calendar_unselected
            ),
            HomeScreenBottomBarItem(description = "Empty", iconSelected = -1, iconUnselected = -1),
            HomeScreenBottomBarItem(
                description = "Profile",
                iconSelected = R.drawable.ic_profile_selected,
                iconUnselected = R.drawable.ic_profile_unselected
            ),
            HomeScreenBottomBarItem(description = "News", iconSelected = R.drawable.ic_news_selected, iconUnselected = R.drawable.ic_news_unselected),
        )
    }

}