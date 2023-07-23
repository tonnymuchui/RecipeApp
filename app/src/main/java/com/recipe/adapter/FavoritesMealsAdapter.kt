package com.recipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recipe.data.Meal
import com.recipe.databinding.MealsItemsBinding

class FavoritesMealsAdapter() :
    RecyclerView.Adapter<FavoritesMealsAdapter.FavoritesMealsAdapterViewHolder>() {
    private var favoriteMeals: List<Meal> = ArrayList()
    private lateinit var onFavoriteClickListener: OnFavoriteClickListener
    private lateinit var onFavoriteLongClickListener: OnFavoriteLongClickListener

    fun setFavoriteMealsList(favoriteMeals: List<Meal>) {
        this.favoriteMeals = favoriteMeals
        notifyDataSetChanged()
    }
    fun getMelaByPosition(position: Int):Meal{
        return favoriteMeals[position]
    }

    fun setOnFavoriteMealClickListener(onFavoriteClickListener: OnFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener
    }

    fun setOnFavoriteLongClickListener(onFavoriteLongClickListener: OnFavoriteLongClickListener) {
        this.onFavoriteLongClickListener = onFavoriteLongClickListener
    }
    inner class FavoritesMealsAdapterViewHolder(val binding: MealsItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesMealsAdapterViewHolder {
        return FavoritesMealsAdapterViewHolder(
            MealsItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoritesMealsAdapterViewHolder, position: Int) {
        holder.binding.apply {
            val meal = differ.currentList[position]
            Glide.with(holder.itemView)
                .load(favoriteMeals[position].strMealThumb)
                .into(holder.binding.imgMeal)
            tvMealName.text = meal.strMeal
        }

        holder.itemView.setOnClickListener {
            onFavoriteClickListener.onFavoriteClick(favoriteMeals[position])
        }

        holder.itemView.setOnLongClickListener {
            onFavoriteLongClickListener.onFavoriteLongCLick(favoriteMeals[position])
            true
        }
    }
    interface OnFavoriteClickListener {
        fun onFavoriteClick(meal: Meal)
    }

    interface OnFavoriteLongClickListener {
        fun onFavoriteLongCLick(meal: Meal)
    }
}
