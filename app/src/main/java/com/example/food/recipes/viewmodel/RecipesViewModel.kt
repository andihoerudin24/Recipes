package com.example.food.recipes.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.food.data.DataStoreRepository
import com.example.food.utils.Constants
import com.example.food.utils.Constants.Companion.API_KEY
import com.example.food.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.food.utils.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.food.utils.Constants.Companion.DETAULT_DIET_TYPE
import com.example.food.utils.Constants.Companion.QUERY_ADD_RECIPES_INFORMATION
import com.example.food.utils.Constants.Companion.QUERY_API_KEY
import com.example.food.utils.Constants.Companion.QUERY_FILLINGRIDEINTS
import com.example.food.utils.Constants.Companion.QUERY_NUMBER
import com.example.food.utils.Constants.Companion.QUERY_SEARCH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor (application: Application, private val dataStoreRepository: DataStoreRepository) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DETAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveBackOnline(backOnline)
    }

    fun applyQueris(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_TYPE] = mealType
        queries[Constants.QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPES_INFORMATION] = "true"
        queries[QUERY_FILLINGRIDEINTS] = "true"
        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        Log.d("pencarian", searchQuery)
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPES_INFORMATION] = "true"
        queries[QUERY_FILLINGRIDEINTS] = "true"
        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We Are Back Online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}
