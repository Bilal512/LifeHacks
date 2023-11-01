package com.simbaone.lifehacks.data.repo

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simbaone.lifehacks.data.models.CategoryDTO
import com.simbaone.lifehacks.data.models.LifeHackDTO
import com.simbaone.lifehacks.domain.repo.DataRepo
import com.simbaone.lifehacks.utils.Resource
import com.simbaone.lifehacks.utils.AssetReader
import javax.inject.Inject

class DataRepoImpl @Inject constructor(
    private val gson: Gson,
    private val assetReader: AssetReader
) : DataRepo {
    override suspend fun getCategories(): Resource<List<CategoryDTO>> {
        val categoriesJson = assetReader.getDataFromAssets("lifehacks/categories.json")

        return if (categoriesJson.isNotBlank()) {
            val myType = object : TypeToken<List<CategoryDTO>>() {}.type
            val categories = gson.fromJson<List<CategoryDTO>>(categoriesJson, myType)
            Resource.Success(categories)
        } else {
            Resource.Error("Error Parsing data")
        }
    }

    override suspend fun getLifeHack(fileName: String): Resource<List<LifeHackDTO>> {
        val lifeHackJson = assetReader.getDataFromAssets("lifehacks/$fileName")
        return if(lifeHackJson.isNotBlank()) {
            val myType = object : TypeToken<List<LifeHackDTO>>() {}.type
            val lifeHacks = gson.fromJson<List<LifeHackDTO>>(lifeHackJson, myType)
            Resource.Success(lifeHacks)
        } else {
            Resource.Error("Error Parsing data")
        }
    }
}