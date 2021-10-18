package com.example.food.recipes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.food.utils.Constants

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    fun applyQueris(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_NUMBER] = "50"
        queries[Constants.QUERY_TYPE] = "snack"
        queries[Constants.QUERY_DIET] = "vegan"
        queries[Constants.QUERY_ADD_RECIPES_INFORMATION] = "true"
        queries[Constants.QUERY_FILLINGRIDEINTS] = "true"
        return queries
    }
}
