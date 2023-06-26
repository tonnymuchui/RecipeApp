package com.recipe.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.recipe.data.Meal
import com.recipe.databinding.FragmentHomeBinding
import com.recipe.viewModel.HomeViewModel

class HomeFragment : Fragment() {
private lateinit var binding: FragmentHomeBinding
private lateinit var homMvvm: HomeViewModel
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
    }

    private fun observeRandomMeal() {
        homMvvm.observeRandomLiveData().observe(viewLifecycleOwner
        ) { value ->
            Glide.with(this@HomeFragment)
                .load(value!!.strMealThumb)
                .into(binding.imgRandomMeal)
        }
    }
}