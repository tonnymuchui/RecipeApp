package com.recipe.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.recipe.R
import com.recipe.data.Meal
import com.recipe.databinding.ActivityMealBinding
import com.recipe.db.MealDatabase
import com.recipe.ui.fragments.HomeFragment
import com.recipe.viewModel.MealViewModel
import com.recipe.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealMvvm: MealViewModel
    private lateinit var binding: ActivityMealBinding
    private lateinit var youtubeLink:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mealDatabase = MealDatabase.getInstance(this)
        val mealViewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, mealViewModelFactory)[MealViewModel::class.java]
        getMealInformationFromIntent()
        setInformationInViews()
        loadingCase()
        mealMvvm.getMealDetails(mealId)
        observeMealDetailsVileData()
        onYoutubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal? = null
    private fun observeMealDetailsVileData() {
        mealMvvm.observeMealDetailsLiveData().observe(
            this
        ) { value ->
            onResponseCase()
            mealToSave = value
            binding.tvCategoryInfo.text = "Category: ${value!!.strCategory}"
            binding.tvAreaInfo.text = "Area ${value!!.strArea}"
            binding.tvInstructions.text = value.strInstructions
            youtubeLink = value.strYoutube.toString()
        }
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext).load(mealThumb).into(binding.imgMealDetails)
        binding.collapsingToolBar.title = mealName
        binding.collapsingToolBar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolBar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }
    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategoryInfo.visibility = View.INVISIBLE
        binding.tvAreaInfo.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategoryInfo.visibility = View.VISIBLE
        binding.tvAreaInfo.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE

    }
}