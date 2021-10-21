package com.example.food.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.food.recipes.model.FoodRecipe
import com.example.food.utils.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}
