package com.example.food.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.food.R
import com.example.food.recipes.model.ExtendedIngredient
import com.example.food.utils.Constants.Companion.BASE_IMAGE_URL
import com.example.food.utils.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.ingredient_imageView.load(BASE_IMAGE_URL + ingredientList[position].image)
        holder.itemView.ingredient_name.text = ingredientList[position].name.capitalize()
        holder.itemView.ingredient_amount.text = ingredientList[position].amount.toString()
        holder.itemView.ingredient_unit.text = ingredientList[position].unit
        holder.itemView.ingredient_consestensy.text = ingredientList[position].consistency
        holder.itemView.ingredient_original.text = ingredientList[position].original
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    fun setData(newingredients: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientList, newingredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientList = newingredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}
