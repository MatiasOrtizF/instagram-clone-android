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
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfo.instagramclone.databinding.FragmentSearchDetailBinding
import com.mfo.instagramclone.ui.search.adapter.SearchAdapter
import com.mfo.instagramclone.utils.ex.clearSessionPreferences
import com.mfo.instagramclone.utils.ex.getToken
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
        searchViewModel.getUsersSearchedHistory(requireContext().getToken())
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
                searchViewModel.addUserSearchedInHistory(requireContext().getToken(), it.userId)
                println(it)
                findNavController().navigate(
                    SearchDetailFragmentDirections.actionSearchDetailFragmentToUserProfileFragment(it.userId, it.userName)
                )
            },
            onHistoryDeleteButtonClicked = { id, position ->
                deleteUserToHistory(id, position)
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
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun initListeners() {
        val token = requireContext().getToken()
        binding.etSearch.addTextChangedListener {
            if(it.toString().trim().isNotEmpty()) {
                searchViewModel.getUserSearchByUserName(token, it.toString())
                // borrar la cruz
            } else {
                searchViewModel.getUsersSearchedHistory(token)
                // agregar la cruz
            }
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
            requireContext().clearSessionPreferences()
        }
    }

    private fun successState(state: SearchState.Success) {
        binding.apply {
            pbSearchDetail.isVisible = false
            rvUserSearch.isVisible = true
        }
        searchAdapter.updateList(state.users)
    }

    private fun goToLogin() {
        findNavController().navigate(
            SearchDetailFragmentDirections.actionSearchDetailFragmentToLoginActivity()
        )
    }

    private fun deleteUserToHistory(id: Long, position: Int) {
        val deletedHistorySuccess: Map<String, Boolean> = mapOf("deleted" to true)
        lifecycleScope.launch {
            val isDelete = searchViewModel.deleteUserSearchedInHistory(requireContext().getToken(), id)
            if (isDelete == deletedHistorySuccess) {
                searchAdapter.onDeleteItem(position)
            }
        }
    }
}