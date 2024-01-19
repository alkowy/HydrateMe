package com.azmarzly.settings.presentation.privacy_policy_settings

import androidx.lifecycle.ViewModel
import com.azmarzly.core.R
import core.domain.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
) : ViewModel() {


    private val _state: MutableStateFlow<PrivacyPolicyState> = MutableStateFlow(PrivacyPolicyState())
    val state = _state.asStateFlow()

    init {
        initialiseState()
    }

    private fun initialiseState() {
        _state.update {
            PrivacyPolicyState(
                privacyPolicyBody = resourceProvider.getString(R.string.privacy_policy_body)
            )
        }
    }
}

data class PrivacyPolicyState(
    val privacyPolicyBody: String = "",
)