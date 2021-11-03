package com.example.food.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.example.food.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.food.utils.Constants.Companion.DETAULT_DIET_TYPE
import com.example.food.utils.Constants.Companion.PREFERENCES_DIETTYPE
import com.example.food.utils.Constants.Companion.PREFERENCES_DIETTYPEID
import com.example.food.utils.Constants.Companion.PREFERENCES_MEALTYPE
import com.example.food.utils.Constants.Companion.PREFERENCES_MEALTYPEID
import com.example.food.utils.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedMealType = preferencesKey<String>(PREFERENCES_MEALTYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEALTYPEID)
        val selectedDietType = preferencesKey<String>(PREFERENCES_DIETTYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIETTYPEID)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES_NAME
    )

    suspend fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        dataStore.edit {
            it[PreferenceKeys.selectedMealType] = mealType
            it[PreferenceKeys.selectedMealTypeId] = mealTypeId
            it[PreferenceKeys.selectedDietType] = dietType
            it[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DETAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }
}

data class MealAndDietType(val selectedMealType: String, val selectedMealTypeId: Int, val selectedDietType: String, val selectedDietTypeId: Int)
