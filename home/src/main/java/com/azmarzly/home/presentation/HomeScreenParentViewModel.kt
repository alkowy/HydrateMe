package com.azmarzly.home.presentation

import androidx.lifecycle.ViewModel
import com.azmarzly.core.R
import com.azmarzly.home.components.HomeScreenBottomBarItem
import core.domain.use_case.UpdateFirestoreUserUseCase
import core.util.HomeRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenParentViewModel @Inject constructor(
    private val firestoreUserUseCase: UpdateFirestoreUserUseCase,
) : ViewModel() {

    companion object {
        val homeScreens = listOf(
            HomeScreenBottomBarItem(
                description = "Home",
                iconSelected = R.drawable.ic_home_selected,
                iconUnselected = R.drawable.ic_home_unselected,
                route = HomeRoute.HOME,
            ),
            HomeScreenBottomBarItem(
                description = "Calendar",
                iconSelected = R.drawable.ic_calendar_selected,
                iconUnselected = R.drawable.ic_calendar_unselected,
                route = HomeRoute.CALENDAR,
            ),
            HomeScreenBottomBarItem(description = "Empty", iconSelected = -1, iconUnselected = -1, route = HomeRoute.HOME_ROOT),
            HomeScreenBottomBarItem(
                description = "Profile",
                iconSelected = R.drawable.ic_profile_selected,
                iconUnselected = R.drawable.ic_profile_unselected,
                route = HomeRoute.PROFILE,
            ),
            HomeScreenBottomBarItem(
                description = "News",
                iconSelected = R.drawable.ic_news_selected,
                iconUnselected = R.drawable.ic_news_unselected,
                route = HomeRoute.NEWS,
            ),
        )
    }

}