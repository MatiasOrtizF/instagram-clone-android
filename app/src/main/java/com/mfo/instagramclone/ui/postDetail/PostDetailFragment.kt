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
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mfo.instagramclone.R
import com.mfo.instagramclone.databinding.FragmentPostDetailBinding
import com.mfo.instagramclone.ui.comment.CommentListDialogFragment
import com.mfo.instagramclone.ui.profile.ProfileFragmentDirections
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
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btnLike.setOnClickListener {

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
            }
            if(state.post.saved != null && state.post.saved == true) {
                btnSave.setImageResource(R.drawable.ic_saved)
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

    }

    private fun openComments() {
        val dialog = CommentListDialogFragment.newInstance(args.postId)
        dialog.show(parentFragmentManager, "CommentListDialogFragment")
    }

    private fun postLikeOrDeleteLike() {

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