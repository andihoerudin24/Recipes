package com.example.food.recipes.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.food.network.Repository
import com.example.food.recipes.model.FoodRecipe
import com.example.food.utils.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

        var recipsResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

        fun getRecipes(queris: Map<String, String>) = viewModelScope.launch {
            getRecipesSaveCall(queris)
        }

        private suspend fun getRecipesSaveCall(queris: Map<String, String>) {
            Log.d("queris", queris.toString())
            recipsResponse.value = NetworkResult.Loading()
            if (hasInternetConnection()) {
                Log.d("internet", "internet ok")
                try {
                    var response = repository.remote.getRecepies(queris)
                    Log.d("responseresponse", response.toString())
                    recipsResponse.value = handleFoodRecipesRespon(response)
                    } catch (e: Exception) {
                    recipsResponse.value = NetworkResult.Error("Recipes Not Found")
                    }
            } else {
                Log.d("internets", "no ok")
                recipsResponse.value = NetworkResult.Error("No Internet Connection")
            }
        }

    private fun handleFoodRecipesRespon(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
            when {
                response.message().toString().contains("timeout") -> {
                    return NetworkResult.Error("Timeout")
                }
                response.code() == 402 -> {
                    return NetworkResult.Error("api key limited")
                }
                response.body()!!.results.isNullOrEmpty() -> {
                    return NetworkResult.Error("Recipes not Found.")
                }
                response.isSuccessful -> {
                    val foodRecipe = response.body()
                    return NetworkResult.Success(foodRecipe!!)
                }
                else -> {
                    return NetworkResult.Error(response.message())
                }
            }
    }

    private fun hasInternetConnection(): Boolean {
            val connectivityManager = getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
    }
}