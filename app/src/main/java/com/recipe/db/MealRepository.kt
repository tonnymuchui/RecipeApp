package com.recipe.db

import androidx.lifecycle.LiveData
import com.recipe.data.Meal

class MealRepository(private val mealDao: MealDao) {
    var mealList : LiveData<List<Meal>> = mealDao.getAllMeals()

    suspend fun insertFavoriteMeal(meal: Meal) {
        mealDao.upsert(meal)
    }
    suspend fun getMealById(mealId: String): Meal {
        return mealDao.getMealById(mealId)
    }

    suspend fun deleteMealById(mealId: String) {
        mealDao.deleteMealById(mealId)
    }

    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)

}