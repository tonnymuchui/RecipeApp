package com.recipe.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.recipe.activities.MainActivity
import com.recipe.adapter.FavoritesMealsAdapter
import com.recipe.data.Meal
import com.recipe.databinding.FragmentFavoritesBinding
import com.recipe.ui.fragments.HomeFragment.Companion.MEAL_ID
import com.recipe.ui.fragments.HomeFragment.Companion.MEAL_NAME
import com.recipe.ui.fragments.HomeFragment.Companion.MEAL_THUMB
import com.recipe.viewModel.HomeViewModel

class FavoritesFragment : Fragment() {
private lateinit var binding: FragmentFavoritesBinding
private lateinit var homeViewModel: HomeViewModel
private lateinit var favoritesMealsAdapter: FavoritesMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesMealsAdapter = FavoritesMealsAdapter()
        homeViewModel = (activity as MainActivity).homeViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView(view)
        observeFavorites()
        onFavoriteMealClick()
        onFavoriteLongMealClick()
        observeBottomDialog()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                homeViewModel.deleteMeal(favoritesMealsAdapter.differ.currentList[position])
                Snackbar.make(requireView(),"Meal Deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        homeViewModel.insertMeal(favoritesMealsAdapter.differ.currentList[position])
                    }
                ).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favRecView)
    }
    private fun observeBottomDialog() {
        homeViewModel.observeBottomSheetMeal().observe(viewLifecycleOwner) {
            val bottomDialog = MealBottomSheetFragment()
            val b = Bundle()
            b.putString(HomeFragment.CATEGORY_NAME, it.strCategory)
            b.putString(HomeFragment.MEAL_NAME, it.strArea?.first().toString())
            b.putString(HomeFragment.MEAL_NAME, it.strMeal?.first().toString())
            b.putString(HomeFragment.MEAL_THUMB, it.strMealThumb?.first().toString())
            b.putString(HomeFragment.MEAL_ID, it.idMeal?.first().toString())
            bottomDialog.arguments = b
            bottomDialog.show(childFragmentManager, "Favorite bottom dialog")
        }
    }
    private fun prepareRecyclerView(v:View) {
        favoritesMealsAdapter = FavoritesMealsAdapter()
        binding.favRecView.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesMealsAdapter
        }
    }

    private fun observeFavorites() {
        homeViewModel.observeFavoritesMealsLiveData().observe(viewLifecycleOwner
        ) { value -> favoritesMealsAdapter.setFavoriteMealsList(value) }
    }
    private fun observeSaveMeal() {
        homeViewModel.observeSaveMeal().observe(viewLifecycleOwner
        ) { t ->
            favoritesMealsAdapter.setFavoriteMealsList(t!!)
            if (t.isEmpty())
                binding.tvFavEmpty.visibility = View.VISIBLE
            else

                binding.tvFavEmpty.visibility = View.GONE
        }
    }
    private fun onFavoriteMealClick(){
        favoritesMealsAdapter.setOnFavoriteMealClickListener(object : FavoritesMealsAdapter.OnFavoriteClickListener{
            override fun onFavoriteClick(meal: Meal) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra(MEAL_ID,meal.idMeal.toString())
                intent.putExtra(MEAL_NAME,meal.strMeal)
                intent.putExtra(MEAL_THUMB,meal.strMealThumb)
                startActivity(intent)
            }

        })
    }

    private fun onFavoriteLongMealClick() {
        favoritesMealsAdapter.setOnFavoriteLongClickListener(object : FavoritesMealsAdapter.OnFavoriteLongClickListener{
            override fun onFavoriteLongCLick(meal: Meal) {
                homeViewModel.getMealByIdBottomSheet(meal.idMeal.toString())
            }

        })
    }

}