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
import com.mfo.instagramclone.ui.search.SearchFragmentDirections
import com.mfo.instagramclone.utils.PreferencesHelper
import com.mfo.instagramclone.utils.PreferencesHelper.set
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        postDetailViewModel.getPost(getToken(), args.postId)
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
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btnLike.setOnClickListener {
                postLikeOrDeleteLike()
            }
            btnComment.setOnClickListener {
                openComments()
            }
            btnSave.setOnClickListener {
                println("save post")
            }
            tvComments.setOnClickListener {
                openComments()
            }
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
            goToLogin()
            clearSessionPreferences()
        }
    }

    private fun successState(state: PostDetailState.Success) {
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
            tvLike.text = state.post.likes.toString() + " likes"
            tvDescription.text = state.post.user.userName
            updateTextWithBoldPrefix(tvDescription, state.post.content)

            tvComments.text = "View all ${state.post.comments} comments"
            tvDate.text = state.post.createdAt
        }
    }

    private fun likeSuccess(likeState: PostDetailState.LikeSuccess) {
        val deletedLikeSuccess: Map<String, Boolean> = mapOf("deleted" to true)
        val postLikeSuccess: Map<String, Boolean> = mapOf("liked" to true)

        val likesString = binding.tvLike.text.toString()
        val currentLikes = likesString.filter { it.isDigit() }.toIntOrNull()
        when (likeState.success) {
            deletedLikeSuccess -> {
                binding.btnLike.setImageResource(R.drawable.ic_like)
                likedPost = false

                if(currentLikes  != null) {
                    val newNumberLikesPost = currentLikes - 1
                    binding.tvLike.text = "$newNumberLikesPost likes"
                }
            }
            postLikeSuccess -> {
                binding.btnLike.setImageResource(R.drawable.ic_liked)
                likedPost = true

                if(currentLikes != null) {
                    val newNumberLikesPost = currentLikes + 1
                    binding.tvLike.text = "$newNumberLikesPost likes"
                }
            }
            else -> {
                Toast.makeText(requireContext(), "Failed to like post", Toast.LENGTH_SHORT).show()
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

    private fun goToLogin() {
        findNavController().navigate(
            PostDetailFragmentDirections.actionPostDetailFragmentToLoginActivity()
        )
    }

    private fun openComments() {
        val dialog = CommentListDialogFragment.newInstance(args.postId)
        dialog.show(parentFragmentManager, "CommentListDialogFragment")
    }

    private fun postLikeOrDeleteLike() {
        val token = getToken()
        if(likedPost) {
            postDetailViewModel.deleteLike(token, args.postId)
        } else {
            postDetailViewModel.addLike(token, args.postId)
        }
    }

    private fun clearSessionPreferences() {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        preferences["jwt"] = ""
    }

    private fun getToken(): String {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        return preferences.getString("jwt", "").toString()
    }
}