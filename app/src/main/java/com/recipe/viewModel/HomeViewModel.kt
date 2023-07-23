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
import com.recipe.db.MealRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Query
import javax.security.auth.callback.Callback

class HomeViewModel(
    private var mealDatabase: MealDatabase
): ViewModel() {
    private val randomMealLive = MutableLiveData<Meal>()
    private var popularItemsMealLive = MutableLiveData<List<MealsByCategory>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetLiveData = MutableLiveData<Meal>()
    private var searchMealsLiveData = MutableLiveData<List<Meal>>()
    private lateinit var repository: MealRepository
    private lateinit var allMeals: LiveData<List<Meal>>

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
    fun getMealById(id:String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : retrofit2.Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal.let {
                    bottomSheetLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }
    fun getMealByIdBottomSheet(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : retrofit2.Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                bottomSheetLiveData.value = response.body()!!.meals?.first()
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        })
    }
    fun observeBottomSheetMeal(): LiveData<Meal> = bottomSheetLiveData

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(
        object : retrofit2.Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList?.let {
                    searchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.e("Search error", t.message.toString())
            }
        }
    )
    fun observeSearchedMealsLiveData() : LiveData<List<Meal>> = searchMealsLiveData
    fun observeCategoriesLiveData():LiveData<List<Category>> = categoryLiveData
    fun observeFavoritesMealsLiveData():LiveData<List<Meal>> = favoritesMealsLiveData
    fun observeSaveMeal(): LiveData<List<Meal>> {
        return allMeals
    }
}