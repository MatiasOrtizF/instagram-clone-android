package com.mfo.instagramclone.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfo.instagramclone.databinding.FragmentSettingBinding
import com.mfo.instagramclone.ui.setting.adapter.SettingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels()
    private lateinit var settingAdapter: SettingAdapter

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
        settingAdapter = SettingAdapter(onItemSelected = {
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToPostActionFragment(getString(it.name)))
        })

        binding.rvSetting.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = settingAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingViewModel.setting.collect {
                    settingAdapter.updateList(it)
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btnDarkMode.setOnClickListener { println("dark mode") }
            btnLogOut.setOnClickListener { println("log out") }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}