package com.mfo.instagramclone.ui.comment

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfo.instagramclone.databinding.FragmentCommentListDialogListDialogBinding
import com.mfo.instagramclone.ui.comment.adapter.CommentAdapter
import com.mfo.instagramclone.utils.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentListDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommentListDialogListDialogBinding? = null
    private val binding get() = _binding!!

    private val commentViewModel: CommentViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter

    companion object {
        fun newInstance(postId: Long): CommentListDialogFragment {
            val fragment = CommentListDialogFragment()
            val args = Bundle()
            args.putLong("postId", postId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token: String = getToken()
        val postId = requireArguments().getLong("postId")
        commentViewModel.getComments(token, postId)
        initUI()
    }

    private fun initUI() {
        initList()
        initUIState()
        initListeners()
    }

    private fun initList() {
        commentAdapter = CommentAdapter(
            onItemSelected = {
                println(it.id)
            },
        )
        binding.rvComment.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                commentViewModel.state.collect {
                    when(it) {
                        CommentState.Loading -> loadingState()
                        is CommentState.Error -> errorState(it.error)
                        is CommentState.Success -> successSate(it)
                    }
                }
            }
        }
    }

    private fun initListeners() {

    }

    private fun loadingState() {
        //binding.pb.isVisible = true
    }

    private fun errorState(error: String) {
        //binding.pb.isVisible = false
        val context = binding.root.context
        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
    }

    private fun successSate(state: CommentState.Success) {
        //binding.pb.isVisible = false
        /*if(state.products.isEmpty()) {
            binding.layoutFavEmpty.isVisible = true
        } else {
            binding.layoutFavEmpty.isVisible = false
            favoriteAdapter.updateList(state.products)
        }*/

        commentAdapter.updateList(state.comments)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentListDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getToken(): String {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        return preferences.getString("jwt", "").toString()
    }
}