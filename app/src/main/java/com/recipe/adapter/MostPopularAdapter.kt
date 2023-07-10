package com.recipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recipe.data.MealsByCategory
import com.recipe.databinding.PopularItemsBinding

class MostPopularAdapter(): RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {
    private var mealList = ArrayList<MealsByCategory>()
    lateinit var onItemClick:((MealsByCategory) -> Unit)
    fun setMeals(mealList: ArrayList<MealsByCategory>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class PopularMealViewHolder(val binding: PopularItemsBinding):RecyclerView.ViewHolder(binding.root)
}