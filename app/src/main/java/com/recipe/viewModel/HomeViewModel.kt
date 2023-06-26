package com.recipe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recipe.api.RetrofitInstance
import com.recipe.data.Meal
import com.recipe.data.MealList
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HomeViewModel(): ViewModel() {
    private val randomMealLive = MutableLiveData<Meal>()
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
    fun observeRandomLiveData():LiveData<Meal> {
        return randomMealLive
    }
}