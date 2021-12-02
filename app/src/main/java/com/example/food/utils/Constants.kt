package com.example.food.utils

class Constants {
    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val API_KEY = "6590d1a099ed46028e02e21b58f6f344"

        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_ADD_RECIPES_INFORMATION = "addRecipeInformation"
        const val QUERY_FILLINGRIDEINTS = "fillIngredients"
        const val QUERY_DIET = "diet"

        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"

        const val PREFERENCES_NAME = "foody_preferences"
        const val DEFAULT_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DETAULT_DIET_TYPE = "gluten free"
        const val PREFERENCES_MEALTYPE = "mealType"
        const val PREFERENCES_MEALTYPEID = "mealTypeId"
        const val PREFERENCES_DIETTYPE = "dietType"
        const val PREFERENCES_DIETTYPEID = "dietTypeId"
        const val PREFERENCES_BACK_ONLINE = "backOnline"
        }
}
