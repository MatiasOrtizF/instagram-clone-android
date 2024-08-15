package com.mfo.instagramclone.ui.userProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mfo.instagramclone.R
import com.mfo.instagramclone.databinding.FragmentUserProfileBinding
import com.mfo.instagramclone.ui.profile.ProfileFragmentDirections
import com.mfo.instagramclone.ui.profile.adapter.ProfileAdapter
import com.mfo.instagramclone.utils.PreferencesHelper
import com.mfo.instagramclone.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private lateinit var profileAdapter: ProfileAdapter

    private val args: UserProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfileViewModel.getUser(getToken(), args.userId)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = args.userName
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
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToPostDetailActivity(it.id),
                )
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
                userProfileViewModel.state.collect {
                    when(it) {
                        UserProfileState.Loading -> loadingState()
                        is UserProfileState.Error -> errorState(it.error)
                        is UserProfileState.Success -> successState(it)
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
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun loadingState() {
        binding.apply {
            pbProfile.isVisible = true
            clProfileInfo.isVisible = false
            llButtons.isVisible = false
            rvProfile.isVisible = false
        }
    }

    private fun errorState(error: String) {
        if(error == "Unauthorized: invalid token") {
            goToLogin()
            clearSessionPreferences()
        }
    }

    private fun successState(state: UserProfileState.Success) {
        binding.apply {
            pbProfile.isVisible = false
            clProfileInfo.isVisible = true
            llButtons.isVisible = true
            rvProfile.isVisible = true
        }
        val context = binding.root.context
        if(state.user.imageProfile != null) {
            Glide.with(context).load(state.user.imageProfile).into(binding.ivProfile)
        }
        if(state.user.post.isEmpty()) {
            binding.tvNoPosts.isVisible = true
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
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToLoginActivity()
        )
    }

    private fun clearSessionPreferences() {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        preferences["jwt"] = ""
    }
}