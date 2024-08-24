package com.mfo.instagramclone.ui.postActions

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.mfo.instagramclone.databinding.FragmentPostActionBinding
import com.mfo.instagramclone.ui.postActions.adapter.PostActionAdapter
import com.mfo.instagramclone.utils.ex.clearSessionPreferences
import com.mfo.instagramclone.utils.ex.getToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostActionFragment : Fragment() {
    private var _binding: FragmentPostActionBinding? = null
    private val binding get() = _binding!!

    private val postActionViewModel: PostActionViewModel by viewModels()
    private val args: PostActionFragmentArgs by navArgs()

    private lateinit var postActionAdapter: PostActionAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = requireContext().getToken()
        println(args.label)
        when(args.label) {
            "Saved" -> postActionViewModel.getAllSave(token)
            "Likes" -> postActionViewModel.getAllLikes(token)
            "Comments" -> postActionViewModel.getAllComments(token)
        }
        initUI()
    }

    private fun initUI() {
        initList()
        initUIState()
    }

    private fun initList() {
        postActionAdapter = PostActionAdapter(
            onItemSelected = {
                findNavController().navigate(PostActionFragmentDirections.actionPostActionFragmentToPostDetailFragment(it.id))
            }
        )
        binding.rvPosts.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = postActionAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                postActionViewModel.state.collect {
                    when(it) {
                        PostActionState.Loading -> loadingState()
                        is PostActionState.Error -> errorState(it.error)
                        is PostActionState.Success -> successState(it)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentPostActionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun loadingState() {
        binding.apply {
            pbPostAction.isVisible = true
            rvPosts.isVisible = false
        }
    }

    private fun errorState(error: String) {
        if(error == "Unauthorized: invalid token") {
            handleGoToLogin()
            requireContext().clearSessionPreferences()
        }
    }

    private fun successState(state: PostActionState.Success) {
        binding.apply {
            pbPostAction.isVisible = false
            rvPosts.isVisible = true
        }
        postActionAdapter.updateList(state.post)
    }

    private fun handleGoToLogin() {
        findNavController().navigate(PostActionFragmentDirections.actionPostActionFragmentToLoginActivity())
    }
}