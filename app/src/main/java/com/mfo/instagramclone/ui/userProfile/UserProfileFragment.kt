package com.mfo.instagramclone.ui.userProfile

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.mfo.instagramclone.utils.ex.clearSessionPreferences
import com.mfo.instagramclone.utils.ex.getToken
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
        userProfileViewModel.getUser(requireContext().getToken(), args.userId)
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
                        is UserProfileState.FollowSuccess -> followSuccess(it)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        val token = requireContext().getToken()
        binding.apply {
            btnAddFollow.setOnClickListener { userProfileViewModel.addFollower(token, args.userId) }
            btnDeleteFollow.setOnClickListener { userProfileViewModel.deleteFollower(token, args.userId) }
            btnFollowers.setOnClickListener { handleGoToFollow("followers") }
            btnFollowing.setOnClickListener { handleGoToFollow("following") }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            handleGoToLogin()
            requireContext().clearSessionPreferences()
        }
    }

    private fun successState(state: UserProfileState.Success) {
        binding.apply {
            pbProfile.isVisible = false
            clProfileInfo.isVisible = true
            llButtons.isVisible = true
            rvProfile.isVisible = true
        }
        if(state.user.followed != null && state.user.followed == true) {
            binding.btnAddFollow.isVisible = false
        } else {
            binding.btnDeleteFollow.isVisible = false
        }
        if(state.user.imageProfile != null) {
            Glide.with(requireContext()).load(state.user.imageProfile).into(binding.ivProfile)
        }
        if(state.user.post.isEmpty()) {
            binding.tvNoPosts.isVisible = true
        }
        binding.tvFullName.text = getString(R.string.full_name_format, state.user.name, state.user.lastName)
        binding.btnPost.text =  updateTextWithBoldPrefix(R.string.btn_posts, state.user.numberPost)
        binding.btnFollowers.text = updateTextWithBoldPrefix(R.string.btn_followers, state.user.numberFollowers)
        binding.btnFollowing.text = updateTextWithBoldPrefix(R.string.btn_followings, state.user.numberFollowing)
        profileAdapter.updateList(state.user.post)
    }

    private fun followSuccess(followState: UserProfileState.FollowSuccess) {
        val deletedFollowerSuccess: Map<String, Boolean> = mapOf("unfollowed" to true)
        val postFollowerSuccess: Map<String, Boolean> = mapOf("following" to true)

        val followersString = binding.btnFollowers.text.toString()
        val currentFollowers = followersString.filter { it.isDigit() }.toIntOrNull()
        when (followState.success) {
            deletedFollowerSuccess -> {
                binding.btnAddFollow.isVisible = true
                binding.btnDeleteFollow.isVisible = false

                if(currentFollowers != null) {
                    val newNumberFollowersUser: Long = (currentFollowers - 1).toLong()
                    binding.btnFollowers.text = updateTextWithBoldPrefix(R.string.btn_followers, newNumberFollowersUser)
                }
            }
            postFollowerSuccess -> {
                binding.btnAddFollow.isVisible = false
                binding.btnDeleteFollow.isVisible = true

                if(currentFollowers != null) {
                    val newNumberFollowersUser = (currentFollowers + 1).toLong()
                    binding.btnFollowers.text = updateTextWithBoldPrefix(R.string.btn_followers, newNumberFollowersUser)
                }
            }
            else -> {
                Toast.makeText(requireContext(), "Failed to follow user", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTextWithBoldPrefix(format: Int, number: Long): SpannableString {
        val spannableString = SpannableString(getString(format, number))

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            number.toString().length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }

    private fun handleGoToLogin() {
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToLoginActivity()
        )
    }

    private fun handleGoToFollow(label: String) {
        findNavController().navigate(UserProfileFragmentDirections.actionUserProfileFragmentToFollowFragment(args.userId, label))
    }
}