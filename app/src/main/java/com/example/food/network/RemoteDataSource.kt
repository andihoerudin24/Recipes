package com.example.food.network

import com.example.food.recipes.model.FoodRecipe
import javax.inject.Inject
import retrofit2.Response

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: Api
) {
    suspend fun getRecepies(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipeApi.getRecipes(queries)
    }
}
