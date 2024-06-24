package com.mfo.instagramclone.ui.postDetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import android.graphics.Typeface
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.mfo.instagramclone.databinding.ActivityPostDetailBinding
import com.mfo.instagramclone.ui.login.LoginActivity
import com.mfo.instagramclone.utils.PreferencesHelper
import com.mfo.instagramclone.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    private val postDetailViewModel: PostDetailViewModel by viewModels()

    private val args: PostDetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        postDetailViewModel.getPost(args.postId)
    }

    private fun initUI() {
        initUISTate()
        initListeners()
    }

    private fun initUISTate() {
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
        println(state.post)
        binding.apply {
            pbPostDetail.isVisible = false
            clPostDetail.isVisible = true
            if(state.post.user.image != null) {
                Glide.with(this@PostDetailActivity).load(state.post.user.image).into(ivPost)
            }
            tvUserName.text = state.post.user.userName
            if(state.post.user.verified) {
                ivVerified.isVisible = true
            }
            Glide.with(this@PostDetailActivity).load(state.post.image).into(ivPost)
            tvLike.text = state.post.likes.toString() + " likes"
            tvDescription.text = state.post.user.userName
            updateTextWithBoldPrefix(tvDescription, state.post.content)

            tvComments.text = "View all ${state.post.comments.toString()} comments"
            tvDate.text = state.post.createdAt.toString()
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

    private fun initListeners() {
        // Registrar el OnBackPressedCallback
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Volver a la actividad anterior
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        binding.btnBack.setOnClickListener {
            callback.handleOnBackPressed()
        }

        binding.btnComment.setOnClickListener {
            println("open comments")
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearSessionPreferences() {
        val context = binding.root.context
        val preferences = PreferencesHelper.defaultPrefs(context)
        preferences["jwt"] = ""
    }
}