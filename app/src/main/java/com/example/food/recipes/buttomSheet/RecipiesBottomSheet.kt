package com.example.food.recipes.buttomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.food.R
import com.example.food.recipes.viewmodel.RecipesViewModel
import com.example.food.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.food.utils.Constants.Companion.DETAULT_DIET_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.Locale
import kotlinx.android.synthetic.main.fragment_recepies_bottom_sheet.view.*

class RecipiesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypechip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DETAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.fragment_recepies_bottom_sheet, container, false)
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypechip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, mView.mealType_chipGroup)
            updateChip(value.selectedDietTypeId, mView.dietType_chipGroup)
        })
        mView.mealType_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypechip = selectedMealType
            mealTypeChipId = selectedChipId
        }
        mView.dietType_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }
        mView.apply_btn.setOnClickListener {
            recipesViewModel.saveMealDietType(
                mealTypechip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            val action =
                RecipiesBottomSheetDirections.actionRecipiesBottomSheetToRecipesFragment2(true)
            findNavController().navigate(action)
        }
        return mView
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipiesBottomSheet", e.message.toString())
            }
        }
    }
}
