package com.mfo.instagramclone.ui.setting

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mfo.instagramclone.R
import com.mfo.instagramclone.databinding.FragmentSettingBinding
import com.mfo.instagramclone.databinding.ModalConfirmationBinding
import com.mfo.instagramclone.ui.setting.adapter.SettingAdapter
import com.mfo.instagramclone.utils.ex.clearSessionPreferences
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
            btnLogOut.setOnClickListener { handleOpenModal() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun handleOpenModal() {
        val dialogCustomBinding = ModalConfirmationBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogCustomBinding.root)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogCustomBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogCustomBinding.btnLogOut.setOnClickListener {
            handleLogOut()
            dialog.dismiss()
        }
    }

    private fun handleLogOut() {
        handleGoToLogin()
        requireContext().clearSessionPreferences()
    }

    private fun handleGoToLogin() {
        findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToLoginActivity())
    }
}