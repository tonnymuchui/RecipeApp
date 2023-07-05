package com.recipe.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.recipe.activities.MealActivity
import com.recipe.data.Meal
import com.recipe.databinding.FragmentHomeBinding
import com.recipe.viewModel.HomeViewModel

class HomeFragment : Fragment() {
private lateinit var binding: FragmentHomeBinding
private lateinit var homMvvm: HomeViewModel
private lateinit var randomMeal:Meal
companion object {
    const val MEAL_ID = "com.recipe.ui.fragments.idMeal"
    const val MEAL_NAME = "com.recipe.ui.fragments.nameMeal"
    const val MEAL_THUMB = "com.recipe.ui.fragments.thumbMeal"
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homMvvm = androidx.lifecycle.ViewModelProvider(this)[HomeViewModel::class.java]
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