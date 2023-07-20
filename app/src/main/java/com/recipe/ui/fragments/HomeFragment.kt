package com.recipe.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.recipe.activities.CategoryMealsActivity
import com.recipe.activities.MainActivity
import com.recipe.activities.MealActivity
import com.recipe.adapter.CategoriesAdapter
import com.recipe.adapter.MostPopularAdapter
import com.recipe.data.Category
import com.recipe.data.MealsByCategory
import com.recipe.data.Meal
import com.recipe.databinding.FragmentHomeBinding
import com.recipe.viewModel.HomeViewModel

class HomeFragment : Fragment() {
private lateinit var binding: FragmentHomeBinding
private lateinit var homMvvm: HomeViewModel
private lateinit var randomMeal:Meal
private lateinit var popularItemsAdapter: MostPopularAdapter
private lateinit var categoriesAdapter: CategoriesAdapter
companion object {
    const val MEAL_ID = "com.recipe.ui.fragments.idMeal"
    const val MEAL_NAME = "com.recipe.ui.fragments.nameMeal"
    const val MEAL_THUMB = "com.recipe.ui.fragments.thumbMeal"
    const val CATEGORY_NAME = "com.recipe.ui.fragments.categoryName"
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homMvvm = (activity as MainActivity).homeViewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homMvvm.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        preparePopularItemsRecyclerView()
        homMvvm.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemsClick()

        prepareCategoriesItemsRecyclerView()
        homMvvm.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()
        onPopularLongClick()
    }

    private fun onPopularLongClick() {
        popularItemsAdapter.onLongItemClick = {meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesItemsRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recyclerCategories.apply {
           layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        homMvvm.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer {categories ->
                categoriesAdapter.setCategoryList(categories as ArrayList<Category>)
        })
    }

    private fun onPopularItemsClick() {
        popularItemsAdapter.onItemClick = {meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        homMvvm.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { value -> popularItemsAdapter.setMeals(mealList = value as ArrayList<MealsByCategory>) }
    }

    private fun observeRandomMeal() {
        homMvvm.observeRandomLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        }
    }
    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }
}