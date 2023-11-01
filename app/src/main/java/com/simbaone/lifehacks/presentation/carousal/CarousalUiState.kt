package com.simbaone.lifehacks.presentation.carousal

import com.simbaone.lifehacks.domain.model.LifeHack

data class CarousalUiState(
    val showCreditButton: Boolean = false,
    val lifeHacks: List<LifeHack> = listOf(),
    val title: String = "",
)
