package com.simbaone.lifehacks.presentation.carousal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.simbaone.lifehacks.base.BaseViewModel
import com.simbaone.lifehacks.constants.Constants
import com.simbaone.lifehacks.constants.Extras
import com.simbaone.lifehacks.data.models.CategoryDTO
import com.simbaone.lifehacks.data.models.LifeHackDTO
import com.simbaone.lifehacks.domain.model.LifeHack
import com.simbaone.lifehacks.domain.repo.DataRepo
import com.simbaone.lifehacks.domain.repo.LogRepo
import com.simbaone.lifehacks.utils.Resource
import com.simbaone.lifehacks.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarousalViewModel @Inject constructor(
    private val dataRepo: DataRepo,
    private val logRepo: LogRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CarousalUiState())
    val uiState = _uiState.asStateFlow()

    private val _channel = Channel<NavigationEvents>()
    val channel = _channel.receiveAsFlow()

    private lateinit var mCategory: CategoryDTO

    init {
        savedStateHandle.get<CategoryDTO>(Extras.CATEGORY)?.let { category ->
            getLifeHacks(category)
            mCategory = category
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        title = mCategory.name,
                        showCreditButton = mCategory.credit != null
                    )
                }
            }
        }
    }

    private fun getLifeHacks(category: CategoryDTO) {
        viewModelScope.launch {
            when (val call = dataRepo.getLifeHack(category.fileName)) {
                is Resource.Error -> logRepo.logError(call.error)
                is Resource.Success -> {
                    val list = call.data.map {
                        LifeHack(
                            data = it.data,
                            spanText = Utils.addReadMoreSpan(it.data),
                            canReadMore = it.data.length > Utils.MAX_LIFE_HACK_LENGTH
                        )
                    }.toMutableList()

                    addNativeAds(list, LifeHack(data = Constants.NATIVE_AD_UUID))

                    logRepo.logError("category = ${category.name} , Size = ${list.size}")

                    _uiState.update { it.copy(lifeHacks = list) }
                }
            }
        }
    }

    private fun addNativeAds(list: MutableList<LifeHack>, elementToAdd: LifeHack) {
        val threshold = 4
        val size = list.size
        val elementsToAdd = size / threshold

        for (i in 1..elementsToAdd) {
            val index = (i * threshold) - 1
            if (index < size) {
                list.add(index, elementToAdd)
            } else {
                // Handle the case where the index is out of bounds
                // You can choose to do nothing or extend the list as needed
                // For example, you can add null values or increase the list size
                list.add(elementToAdd)
            }
        }
    }

    fun onCreditButtonClick() {
        viewModelScope.launch {
            mCategory.credit?.let { _channel.send(NavigationEvents.LaunchBrowser(it)) }
        }
    }


    sealed class NavigationEvents {
        data class LaunchBrowser(val link: String) : NavigationEvents()
    }
}

