package com.recipe.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.recipe.R
import com.recipe.activities.MainActivity
import com.recipe.adapter.FavoritesMealsAdapter
import com.recipe.databinding.FragmentSearchBinding
import com.recipe.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
private lateinit var binding: FragmentSearchBinding
private lateinit var homeViewModel: HomeViewModel
private lateinit var searchRecyclerViewAdapter: FavoritesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).homeViewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerview()
        binding.icSearch.setOnClickListener { searchMeals() }
        observeSearchedMealsLiveData()
        var searchJob: Job?=null
        binding.edSearch.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(300)
                homeViewModel.searchMeals(it.toString())
            }
        }
    }

    private fun observeSearchedMealsLiveData() {
        homeViewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner, Observer {
            searchRecyclerViewAdapter.differ.submitList(it)
        })
    }

    private fun searchMeals() {
    val searchQuery = binding.edSearch.text.toString()
    if (searchQuery.isNotEmpty()) {
        homeViewModel.searchMeals(searchQuery)
    }
}
    private fun prepareRecyclerview() {
        searchRecyclerViewAdapter = FavoritesMealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }
}