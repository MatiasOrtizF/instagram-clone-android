package com.mfo.instagramclone.ui.postDetail

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mfo.instagramclone.R
import com.mfo.instagramclone.databinding.FragmentPostDetailBinding
import com.mfo.instagramclone.ui.comment.CommentListDialogFragment
import com.mfo.instagramclone.utils.ex.clearSessionPreferences
import com.mfo.instagramclone.utils.ex.getToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailFragment : Fragment() {
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val postDetailViewModel: PostDetailViewModel by viewModels()

    private val args: PostDetailFragmentArgs by navArgs()

    private var likedPost: Boolean = false
    private var savedPost: Boolean = false
    private var userName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        postDetailViewModel.getPost(requireContext().getToken(), args.postId)
    }

    private fun initUI() {
        initList()
        initUIState()
        initListeners()
    }

    private fun initList() {

    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                postDetailViewModel.state.collect {
                    when(it) {
                        PostDetailState.Loading -> loadingState()
                        is PostDetailState.Error -> errorState(it.error)
                        is PostDetailState.Success -> successState(it)
                        is PostDetailState.LikeSuccess -> likeSuccess(it)
                        is PostDetailState.SaveSuccess -> saveSuccess(it)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btnLike.setOnClickListener { postLikeOrDeleteLike() }
            btnComment.setOnClickListener { openComments() }
            btnSave.setOnClickListener { postSaveOrUnSave() }
            btnLikes.setOnClickListener { handleGoToLikes() }
            btnComments.setOnClickListener { openComments() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    private fun loadingState() {
        binding.pbPostDetail.isVisible = true
    }

    private fun errorState(error: String) {
        binding.pbPostDetail.isVisible = false
        if(error == "Unauthorized: invalid token") {
            handleGoToLogin()
            requireContext().clearSessionPreferences()
        }
    }

    private fun successState(state: PostDetailState.Success) {
        userName = state.post.user.userName
        binding.apply {
            pbPostDetail.isVisible = false
            clPostDetail.isVisible = true
            if(state.post.user.image != null) {
                Glide.with(requireContext()).load(state.post.user.image).into(ivPost)
            }
            if(state.post.liked != null && state.post.liked == true) {
                btnLike.setImageResource(R.drawable.ic_liked)
                likedPost = true
            }
            if(state.post.saved != null && state.post.saved == true) {
                btnSave.setImageResource(R.drawable.ic_saved)
                savedPost = true
            }
            tvUserName.text = state.post.user.userName
            if(state.post.user.verified) {
                ivVerified.isVisible = true
            }
            Glide.with(requireContext()).load(state.post.image).into(ivPost)
            btnLikes.text = getString(R.string.btn_likes, state.post.likes)
            tvDescription.text = state.post.user.userName
            updateTextWithBoldPrefix(tvDescription, state.post.content)

            btnComments.text = getString(R.string.btn_comments, state.post.comments)
            tvDate.text = state.post.createdAt
        }
    }

    private fun likeSuccess(likeState: PostDetailState.LikeSuccess) {
        val deletedLikeSuccess: Map<String, Boolean> = mapOf("deleted" to true)
        val postLikeSuccess: Map<String, Boolean> = mapOf("liked" to true)

        val likesString = binding.btnLikes.text.toString()
        val currentLikes = likesString.filter { it.isDigit() }.toIntOrNull()
        when (likeState.success) {
            deletedLikeSuccess -> {
                binding.btnLike.setImageResource(R.drawable.ic_like)
                likedPost = false

                if(currentLikes  != null) {
                    val newNumberLikesPost = currentLikes - 1
                    binding.btnLikes.text = getString(R.string.btn_likes, newNumberLikesPost)
                }
            }
            postLikeSuccess -> {
                binding.btnLike.setImageResource(R.drawable.ic_liked)
                likedPost = true

                if(currentLikes != null) {
                    val newNumberLikesPost = currentLikes + 1
                    binding.btnLikes.text = getString(R.string.btn_likes, newNumberLikesPost)
                }
            }
            else -> {
                Toast.makeText(requireContext(), "Failed to like post", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveSuccess(saveState: PostDetailState.SaveSuccess) {
        val deletedSaveSuccess: Map<String, Boolean> = mapOf("deleted" to true)
        val postSaveSuccess: Map<String, Boolean> = mapOf("saved" to true)

        when (saveState.success) {
            deletedSaveSuccess -> {
                binding.btnSave.setImageResource(R.drawable.ic_save)
                savedPost = false
            }
            postSaveSuccess -> {
                binding.btnSave.setImageResource(R.drawable.ic_saved)
                savedPost = true
            }
            else -> {
                Toast.makeText(requireContext(), "Failed to save post", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTextWithBoldPrefix(textView: TextView, suffix: String) {
        val prefix = textView.text.toString()
        val combinedText = "$prefix $suffix"
        val spannableString = SpannableString(combinedText)

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            prefix.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableString
    }

    private fun handleGoToLikes() {
        findNavController().navigate(
            PostDetailFragmentDirections.actionPostDetailFragmentToFollowFragment(args.postId, "Likes")
        )
    }

    private fun handleGoToLogin() {
        findNavController().navigate(
            PostDetailFragmentDirections.actionPostDetailFragmentToLoginActivity()
        )
    }

    private fun openComments() {
        val dialog = CommentListDialogFragment.newInstance(args.postId, userName)
        dialog.show(parentFragmentManager, "CommentListDialogFragment")
    }

    private fun postLikeOrDeleteLike() {
        val token = requireContext().getToken()
        if(likedPost) {
            postDetailViewModel.deleteLike(token, args.postId)
        } else {
            postDetailViewModel.addLike(token, args.postId)
        }
    }

    private fun postSaveOrUnSave() {
        val token = requireContext().getToken()
        if(savedPost) {
            postDetailViewModel.deleteSave(token, args.postId)
        } else {
            postDetailViewModel.addSave(token, args.postId)
        }
    }
}