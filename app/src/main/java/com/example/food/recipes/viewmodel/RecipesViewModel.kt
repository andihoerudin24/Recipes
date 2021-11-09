package com.example.food.recipes.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.food.data.DataStoreRepository
import com.example.food.utils.Constants
import com.example.food.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.food.utils.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.food.utils.Constants.Companion.DETAULT_DIET_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor (application: Application, private val dataStoreRepository: DataStoreRepository) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DETAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun applyQueris(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_TYPE] = mealType
        queries[Constants.QUERY_DIET] = dietType
        queries[Constants.QUERY_ADD_RECIPES_INFORMATION] = "true"
        queries[Constants.QUERY_FILLINGRIDEINTS] = "true"
        return queries
    }
}
