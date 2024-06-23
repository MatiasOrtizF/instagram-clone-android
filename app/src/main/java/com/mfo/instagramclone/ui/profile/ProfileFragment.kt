package com.mfo.instagramclone.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mfo.instagramclone.databinding.FragmentProfileBinding
import com.mfo.instagramclone.ui.profile.adapter.ProfileAdapter
import com.mfo.instagramclone.utils.PreferencesHelper
import com.mfo.instagramclone.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var profileAdapter: ProfileAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token: String = getToken()
        profileViewModel.getUserInfo(token)
        initUI()
    }

    private fun initUI() {
        initList()
        initUIState()
        initListeners()
    }

    private fun initList() {
        profileAdapter = ProfileAdapter(
            onItemSelected = {
                println(it.id)
            }
        )
        binding.rvProfile.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = profileAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.state.collect {
                    when(it) {
                        ProfileState.Loading -> loadingState()
                        is ProfileState.Error -> errorState(it.error)
                        is ProfileState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun initListeners() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun loadingState() {
        binding.apply {
            pbProfile.isVisible = true
            clProfileInfo.isVisible = false
            btnEditProfile.isVisible = false
            rvProfile.isVisible = false
        }
    }

    private fun errorState(error: String) {
        if(error == "Unauthorized: invalid token") {
            goToLogin()
            clearSessionPreferences()
        }
    }

    private fun successState(state: ProfileState.Success) {
        binding.apply {
            pbProfile.isVisible = false
            clProfileInfo.isVisible = true
            btnEditProfile.isVisible = true
            rvProfile.isVisible = true
        }
        val context = binding.root.context
        if(state.user.imageProfile != null) {
            Glide.with(context).load(state.user.imageProfile).into(binding.ivProfile)
        }
        binding.tvFullName.text = state.user.name + " " + state.user.lastName
        binding.tvNumberPost.text = state.user.numberPost.toString()
        binding.tvNumberFollowers.text = state.user.numberFollowers.toString()
        binding.tvNumberFollowing.text = state.user.numberFollowing.toString()
        profileAdapter.updateList(state.user.post)
    }

    private fun getToken(): String {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        return preferences.getString("jwt", "").toString()
    }

    private fun goToLogin() {
        println("go to login")
    }

    private fun clearSessionPreferences() {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        preferences["jwt"] = ""
    }
}