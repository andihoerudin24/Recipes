package com.example.food.recipes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.food.utils.Constants
import com.example.food.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.food.utils.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.food.utils.Constants.Companion.DETAULT_DIET_TYPE

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    fun applyQueris(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_TYPE] = DEFAULT_MEAL_TYPE
        queries[Constants.QUERY_DIET] = DETAULT_DIET_TYPE
        queries[Constants.QUERY_ADD_RECIPES_INFORMATION] = "true"
        queries[Constants.QUERY_FILLINGRIDEINTS] = "true"
        return queries
    }
}
