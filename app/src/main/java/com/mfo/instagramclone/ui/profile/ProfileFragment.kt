package com.mfo.instagramclone.ui.profile

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mfo.instagramclone.R
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
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToPostDetailActivity(it.id)
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

    private fun successState(state: ProfileState.Success) {
        binding.apply {
            pbProfile.isVisible = false
            clProfileInfo.isVisible = true
            llButtons.isVisible = true
            rvProfile.isVisible = true

            tvFullName.text = getString(R.string.full_name_format, state.user.name, state.user.lastName)
            btnPost.text = updateTextWithBoldPrefix(R.string.btn_posts, state.user.numberPost)
            btnFollowers.text = updateTextWithBoldPrefix(R.string.btn_followers, state.user.numberFollowers)
            btnFollowing.text = updateTextWithBoldPrefix(R.string.btn_followings, state.user.numberFollowing)
        }
        if(state.user.imageProfile != null) {
            Glide.with(requireContext()).load(state.user.imageProfile).into(binding.ivProfile)
        }
        profileAdapter.updateList(state.user.post)
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

    private fun getToken(): String {
        val preferences = PreferencesHelper.defaultPrefs(requireContext())
        return preferences.getString("jwt", "").toString()
    }

    private fun goToLogin() {
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToLoginActivity()
        )
    }

    private fun clearSessionPreferences() {
        val preferences = PreferencesHelper.defaultPrefs(requireContext())
        preferences["jwt"] = ""
    }
}