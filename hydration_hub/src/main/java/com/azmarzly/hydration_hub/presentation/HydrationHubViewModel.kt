package com.azmarzly.hydration_hub.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azmarzly.hydration_hub.domain.GetHydrationContentUseCase
import core.DispatcherIO
import core.model.HydrationHubContentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HydrationHubViewModel @Inject constructor(
    private val getHydrationContentUseCase: GetHydrationContentUseCase,
    @DispatcherIO private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {


    private val _state: MutableStateFlow<HydrationHubState> = MutableStateFlow(HydrationHubState())
    val state = _state.asStateFlow()

    init {
        fetchContent()
    }

    fun fetchContent() {
        viewModelScope.launch(ioDispatcher) {
            _state.update {
                it.copy(
                    isLoading = true,
                    isError = false
                )
            }
            val hydrationHubContent = getHydrationContentUseCase.invoke()
            if (hydrationHubContent != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        content = hydrationHubContent
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                    )
                }
            }

        }
    }
}

data class HydrationHubState(
    val content: HydrationHubContentModel? = HydrationHubContentModel(emptyList(), emptyList()),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
)
