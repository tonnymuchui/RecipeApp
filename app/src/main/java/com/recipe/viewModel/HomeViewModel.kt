package com.recipe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipe.api.RetrofitInstance
import com.recipe.data.Category
import com.recipe.data.CategoryList
import com.recipe.data.MealsByCategoryList
import com.recipe.data.MealsByCategory
import com.recipe.data.Meal
import com.recipe.data.MealList
import com.recipe.db.MealDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HomeViewModel(
    private var mealDatabase: MealDatabase
): ViewModel() {
    private val randomMealLive = MutableLiveData<Meal>()
    private var popularItemsMealLive = MutableLiveData<List<MealsByCategory>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().geAllMeals()
    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback,
            retrofit2.Callback<MealList> {

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal : Meal = response.body()!!.meals[0]
                    randomMealLive.value = randomMeal
                } else return;
            }
        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : retrofit2.Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() !=null) {
                    popularItemsMealLive.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("Home Fragment", t.message.toString())
            }
        })
    }
    fun observeRandomLiveData():LiveData<Meal> {
        return randomMealLive
    }
    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsMealLive
    }
    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : retrofit2.Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
               if (response.body() !=null) {
                   categoryLiveData.value = response.body()!!.categories
               }
            }
            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Home Fragment", t.message.toString())
            }
        })
    }
    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoryLiveData
    }
    fun observeFavoritesMealsLiveData():LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }
}