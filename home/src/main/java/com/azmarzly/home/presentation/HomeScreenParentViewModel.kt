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


    private val _state: MutableStateFlow<HomeScreenRootState> = MutableStateFlow(HomeScreenRootState.EmptyState)
    val state: StateFlow<HomeScreenRootState> = _state.asStateFlow()


    fun updateUserData(userDataModel: UserDataModel) {
        viewModelScope.launch {
            firestoreUserUseCase.invoke(userDataModel.toFirestoreUserDataModel()).collect { updateResult ->
                when (updateResult) {
                    Resource.EmptyState -> doNothing()
                    is Resource.Error -> {
                        _state.update { HomeScreenRootState.Error(updateResult.errorMessage ?: "Unexpected error") }
                    }

                    Resource.Loading -> _state.update { HomeScreenRootState.Loading }
                    is Resource.Success -> _state.update { HomeScreenRootState.EmptyState }
                }
            }
        }
    }

}

sealed interface HomeScreenRootState {
    object Loading : HomeScreenRootState
    data class Error(val message: String) : HomeScreenRootState
    object EmptyState : HomeScreenRootState
}