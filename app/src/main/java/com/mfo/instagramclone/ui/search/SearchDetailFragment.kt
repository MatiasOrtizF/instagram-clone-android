package com.mfo.instagramclone.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfo.instagramclone.databinding.FragmentSearchDetailBinding
import com.mfo.instagramclone.ui.postDetail.PostDetailState
import com.mfo.instagramclone.ui.profile.ProfileFragmentDirections
import com.mfo.instagramclone.ui.profile.adapter.ProfileAdapter
import com.mfo.instagramclone.ui.search.adapter.SearchAdapter
import com.mfo.instagramclone.utils.PreferencesHelper
import com.mfo.instagramclone.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchDetailFragment : Fragment() {
    private var _binding: FragmentSearchDetailBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initList()
        initUIState()
        initListeners()
    }

    private fun initList() {
        searchAdapter = SearchAdapter(
            onItemSelected = {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToPostDetailActivity(it.id)
                )
            }
        )
        binding.rvUserSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.state.collect {
                    when(it) {
                        SearchState.Loading -> loadingState()
                        is SearchState.Error -> errorState(it.error)
                        is SearchState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        val token = getToken()
        binding.etSearch.addTextChangedListener {
            println(it.toString())
            searchViewModel.getUserByUserName(token, it.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun loadingState() {
        binding.apply {
            pbSearchDetail.isVisible = true
            rvUserSearch.isVisible = false
        }
    }

    private fun errorState(error: String) {
        if(error == "Unauthorized: invalid token") {
            goToLogin()
            clearSessionPreferences()
        }
    }

    private fun successState(state: SearchState.Success) {
        binding.apply {
            pbSearchDetail.isVisible = false
            rvUserSearch.isVisible = true
        }
        println(state.user)
        searchAdapter.updateList(state.user)
    }

    private fun getToken(): String {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        return preferences.getString("jwt", "").toString()
    }

    private fun goToLogin() {
        findNavController().navigate(
            SearchDetailFragmentDirections.actionSearchDetailFragmentToLoginActivity()
        )
    }

    private fun clearSessionPreferences() {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        preferences["jwt"] = ""
    }
}