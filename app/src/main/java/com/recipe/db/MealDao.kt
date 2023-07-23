package com.recipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.recipe.data.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal:Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM mealInformation ORDER BY idMeal ASC")
    fun getAllMeals(): LiveData<List<Meal>>

    @Query("SELECT * FROM mealInformation WHERE idMeal = :id")
    fun getMealById(id: String): Meal

    @Query("DELETE FROM mealInformation WHERE idMeal =:id")
    fun deleteMealById(id:String)

    @Delete
    fun deleteMeal(meal: Meal)
}