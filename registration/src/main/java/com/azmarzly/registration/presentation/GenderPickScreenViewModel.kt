package com.azmarzly.registration.presentation

import androidx.lifecycle.ViewModel
import core.domain.ResourceProvider
import core.model.Gender
import core.model.GenderState
import core.model.toDrawableIconRes
import core.model.toNameResourceStringId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GenderPickScreenViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    private val _gendersState: MutableStateFlow<List<GenderState>> = MutableStateFlow(Gender.entries.map {
        GenderState(
            name = resourceProvider.getString(it.toNameResourceStringId()),
            gender = it,
            genderIcon = it.toDrawableIconRes()
        )
    })

    val genderState = _gendersState.asStateFlow()

}