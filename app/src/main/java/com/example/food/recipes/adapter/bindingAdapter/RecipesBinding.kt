package com.example.food.recipes.adapter.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.food.data.RecipesEntity
import com.example.food.recipes.model.FoodRecipe
import com.example.food.utils.NetworkResult

class RecipesBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe?>,
            database: List<RecipesEntity>
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
                } else if (apiResponse is NetworkResult.Loading) {
                    imageView.visibility = View.INVISIBLE
                    } else if (apiResponse is NetworkResult.Success) {
                imageView.visibility = View.INVISIBLE
                }
        }

        @BindingAdapter("readApiRespinse2", "readdatabase2", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
                } else if (apiResponse is NetworkResult.Loading) {
                            textView.visibility = View.INVISIBLE
                } else if (apiResponse is NetworkResult.Success) {
                textView.visibility = View.INVISIBLE
                }
        }
    }
}
