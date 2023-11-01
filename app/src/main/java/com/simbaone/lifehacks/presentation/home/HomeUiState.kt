package com.simbaone.lifehacks.presentation.home

import com.simbaone.lifehacks.data.models.CategoryDTO

data class HomeUiState(
    val title: String = "",
    val categories: List<CategoryDTO> = listOf()
)
