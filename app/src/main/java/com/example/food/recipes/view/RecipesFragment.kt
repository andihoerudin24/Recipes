package com.example.food.recipes.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.R
import com.example.food.recipes.adapter.RecipesAdapter
import com.example.food.recipes.viewmodel.MainViewModel
import com.example.food.recipes.viewmodel.RecipesViewModel
import com.example.food.utils.NetworkListener
import com.example.food.utils.NetworkResult
import com.example.food.utils.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<RecipesFragmentArgs>()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mView: View
    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)
        setHasOptionsMenu(true)
        setupReclerView()
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
        })
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvaliablility(requireContext())
                .collect { status ->
                    Log.d("networklistener", status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }
        initOnclick()
        return mView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searcView = search.actionView as? SearchView
        searcView?.isSubmitButtonEnabled = true
        searcView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun setupReclerView() {
        mView.recyclerview.adapter = mAdapter
        mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipe.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty() && !args.backFromButtomSheet) {
                    Log.d("recipesfragment", "read database called")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestDataApi()
                }
            })
        }
    }

    private fun requestDataApi() {
        Log.d("recipesfragment", "request api data called")
        mainViewModel.getRecipes(recipesViewModel.applyQueris())
        mainViewModel.recipsResponse.observe(viewLifecycleOwner, { response ->
            Log.d("responseee", response.toString())
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.serachRecipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipe.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                    }
                })
            }
    }

    private fun showShimmerEffect() {
        mView.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        mView.recyclerview.hideShimmer()
    }

    private fun initOnclick() {
        mView.recipes_fab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipiesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }
    }
}
