package com.simbaone.lifehacks.presentation.home

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.simbaone.lifehacks.base.BaseViewModel
import com.simbaone.lifehacks.data.models.CategoryDTO
import com.simbaone.lifehacks.domain.repo.DataRepo
import com.simbaone.lifehacks.domain.repo.LogRepo
import com.simbaone.lifehacks.firebase.EventLogger
import com.simbaone.lifehacks.firebase.EventTypeUI
import com.simbaone.lifehacks.utils.ColorUtils
import com.simbaone.lifehacks.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepo: DataRepo,
    private val logRepo: LogRepo,
    private val context: Context
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _channel = Channel<NavigationEvents>()
    val channel = _channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(title = "All Categories") }
        }
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            when (val call = dataRepo.getCategories()) {
                is Resource.Error -> {
                    logRepo.logError(call.error)
                }

                is Resource.Success -> {
                    _uiState.update { state ->
                        state.copy(categories = call.data.map {
                            it.copy(bgColor = ColorUtils.generateColorFromString(it.name))
                        })
                    }
                }
            }
        }
    }

    fun onItemClick(category: CategoryDTO) {
        viewModelScope.launch {
            EventLogger.logFirebaseUIEvent(context, EventTypeUI.CATEOGRY, category.name)
            _channel.send(NavigationEvents.NavigateToLifeHacks(category))
        }
    }

    sealed class NavigationEvents {
        data class NavigateToLifeHacks(val category: CategoryDTO) : NavigationEvents()
    }

}