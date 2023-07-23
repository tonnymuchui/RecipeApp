package com.recipe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipe.api.RetrofitInstance
import com.recipe.data.Meal
import com.recipe.data.MealList
import com.recipe.db.MealDatabase
import com.recipe.db.MealRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    private val mealDatabase: MealDatabase
): ViewModel() {
    private val mealDetailsLiveData = MutableLiveData<Meal>()
    private lateinit var repository: MealRepository

    fun getMealDetails(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                } else return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealFragment", t.message.toString())
            }
        })
    }

    fun observeMealDetailsLiveData(): LiveData<Meal> {
        return mealDetailsLiveData
    }
    fun geAllMeals() {
        viewModelScope.launch(Dispatchers.Main) {
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoriteMeal(meal)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun deleteMeal(meal:Meal) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMeal(meal)
    }
}