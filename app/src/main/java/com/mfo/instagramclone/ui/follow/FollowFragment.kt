package com.mfo.instagramclone.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfo.instagramclone.R
import com.mfo.instagramclone.databinding.FragmentFollowBinding
import com.mfo.instagramclone.ui.follow.adapter.FollowAdapter
import com.mfo.instagramclone.utils.ex.clearSessionPreferences
import com.mfo.instagramclone.utils.ex.getToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val followViewModel: FollowViewModel by viewModels()
    private lateinit var followAdapter: FollowAdapter

    private val args: FollowFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = args.label
        val token = requireContext().getToken()
        when(args.label) {
            "Followers" -> followViewModel.getFollowers(token, args.userId)
            "Following" -> followViewModel.getFollowings(token, args.userId)
            "Likes" -> followViewModel.getUsersLikedPost(token, args.userId)
        }
        initUI()
    }

    private fun initUI() {
        initList()
        initUIState()
        initListeners()
    }

    private fun initList() {
        followAdapter = FollowAdapter(
            onItemSelected = {
                findNavController().navigate(
                    FollowFragmentDirections.actionFollowFragmentToUserProfileFragment(it.id, it.userName)
                )
            },
            onFollowToggleButtonClick = { id, followed, position ->
                handleDeleteOrPostFollower(id, followed, position)
                //deleteUserToHistory(id, position)
            }
        )
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = followAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                followViewModel.state.collect {
                    when(it) {
                        FollowState.Loading -> loadingState()
                        is FollowState.Error -> errorState(it.error)
                        is FollowState.Success -> successState(it)
                        is FollowState.FollowSuccess -> followSuccess(it)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.etSearch.addTextChangedListener {
            followAdapter.filterList(it.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun loadingState() {
        binding.apply {
            pbFollow.isVisible = true
            rvUsers.isVisible = false
        }
    }

    private fun errorState(error: String) {
        if(error == "Unauthorized: invalid token") {
            handleGoToLogin()
            requireContext().clearSessionPreferences()
        }
    }

    private fun successState(state: FollowState.Success) {
        binding.pbFollow.isVisible = false
        if(state.users.isEmpty()) {
            binding.tvNoUsers.isVisible = true
        } else {
            binding.rvUsers.isVisible = true
            followAdapter.updateList(state.users)
        }
    }

    private fun followSuccess(followState: FollowState.FollowSuccess) {
        val postFollowerSuccess: Map<String, Boolean> = mapOf("following" to true)
        val deletedFollowerSuccess: Map<String, Boolean> = mapOf("unfollowed" to true)

        when (followState.success) {
            postFollowerSuccess -> {
                followAdapter.updateFollowState(followState.position, true)
            }
            deletedFollowerSuccess -> {
                followAdapter.updateFollowState(followState.position, false)
            }
            else -> {
                Toast.makeText(requireContext(), "Failed to follow user", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleDeleteOrPostFollower(userId: Long, followed: Boolean, position: Int) {
        val token = requireContext().getToken()
        if(followed) {
            followViewModel.deleteFollower(token, userId, position)
        } else {
            followViewModel.addFollower(token, userId, position)
        }
    }

    private fun handleGoToLogin() {
        findNavController().navigate(
            FollowFragmentDirections.actionFollowFragmentToLoginActivity()
        )
    }
}