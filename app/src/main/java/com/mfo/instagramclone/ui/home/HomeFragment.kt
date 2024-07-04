package com.mfo.instagramclone.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mfo.instagramclone.R
import com.mfo.instagramclone.databinding.FragmentHomeBinding
import com.mfo.instagramclone.databinding.FragmentPostDetailBinding
import com.mfo.instagramclone.ui.profile.ProfileFragmentDirections
import com.mfo.instagramclone.utils.PreferencesHelper
import com.mfo.instagramclone.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogOut.setOnClickListener {
            goToLogin()
            clearSessionPreferences()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun goToLogin() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToLoginActivity()
        )
    }

    private fun clearSessionPreferences() {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        preferences["jwt"] = ""
    }
}