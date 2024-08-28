package com.mfo.instagramclone.ui.comment

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfo.instagramclone.R
import com.mfo.instagramclone.databinding.FragmentCommentListDialogListDialogBinding
import com.mfo.instagramclone.ui.comment.adapter.CommentAdapter
import com.mfo.instagramclone.utils.ex.getToken
import com.mfo.instagramclone.utils.ex.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentListDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommentListDialogListDialogBinding? = null
    private val binding get() = _binding!!

    private val commentViewModel: CommentViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter


    companion object {
        fun newInstance(postId: Long, userName: String): CommentListDialogFragment {
            val fragment = CommentListDialogFragment()
            val args = Bundle()
            args.putLong("postId", postId)
            args.putString("userName", userName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = requireContext().getToken()
        val postId = requireArguments().getLong("postId")
        val userName = requireArguments().getString("userName")
        commentViewModel.getComments(token, postId)
        binding.etComment.hint = getString(R.string.hint_comment, userName)
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
            onItemLiked = { commentId, position, liked ->
                println("like comment: $commentId en la posicion $position")
                handleDeleteOrAddLike(commentId, liked, position)
            }
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
                        is CommentState.SendSuccess -> sendSuccess(it)
                        is CommentState.LikeSuccess -> likeSuccess(it)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnSend.setOnClickListener { handleSendMessage(binding.etComment.text.toString()) }
    }

    private fun loadingState() {
        binding.pbComment.isVisible = true
        binding.rvComment.isVisible = false
    }

    private fun errorState(error: String) {
        binding.pbComment.isVisible = false
        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
    }

    private fun successSate(state: CommentState.Success) {
        binding.pbComment.isVisible = false
        if(state.comments.isEmpty()) {
            binding.llEmptyComments.isVisible = true
            binding.rvComment.isVisible = false
        } else {
            commentAdapter.updateList(state.comments)
            binding.llEmptyComments.isVisible = false
            binding.rvComment.isVisible = true
        }
    }

    private fun sendSuccess(state: CommentState.SendSuccess) {
        commentAdapter.onAddItem(state.comment)
        binding.pbComment.isVisible = false
        binding.rvComment.isVisible = true
        binding.btnSend.isEnabled = true
    }

    private fun likeSuccess(state: CommentState.LikeSuccess) {
        val postLikeSuccess: Map<String, Boolean> = mapOf("liked" to true)
        val deletedLikeSuccess: Map<String, Boolean> = mapOf("deleted" to true)

        when (state.success) {
            postLikeSuccess -> {
                commentAdapter.updateLikeState(state.position, true)
            }
            deletedLikeSuccess -> {
                commentAdapter.updateLikeState(state.position, false)
            }
            else -> {
                Toast.makeText(requireContext(), "Failed to like post", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun handleSendMessage(comment: String) {
        if(comment.trim().isNotEmpty()) {
            val postId = requireArguments().getLong("postId")
            commentViewModel.addComment(requireContext().getToken(), postId, comment)
            hideKeyboard()
            binding.etComment.setText("")
            binding.btnSend.isEnabled = false
        }
    }

    private fun handleDeleteOrAddLike(commentId: Long, liked: Boolean, position: Int) {
        if(liked) {
            commentViewModel.deleteCommentLike(requireContext().getToken(), commentId, position)
        } else {
            commentViewModel.addCommentLike(requireContext().getToken(), commentId, position)
        }
    }
}