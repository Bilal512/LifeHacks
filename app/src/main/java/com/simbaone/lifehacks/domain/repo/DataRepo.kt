package com.simbaone.lifehacks.domain.repo

import com.simbaone.lifehacks.data.models.CategoryDTO
import com.simbaone.lifehacks.data.models.LifeHackDTO
import com.simbaone.lifehacks.utils.Resource

interface DataRepo {

    suspend fun getCategories(): Resource<List<CategoryDTO>>

    suspend fun getLifeHack(fileName: String): Resource<List<LifeHackDTO>>
}