package com.mfo.instagramclone.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mfo.instagramclone.R
import com.mfo.instagramclone.databinding.ActivityLoginBinding
import com.mfo.instagramclone.domain.models.LoginRequest
import com.mfo.instagramclone.ui.main.MainActivity
import com.mfo.instagramclone.utils.PreferencesHelper
import com.mfo.instagramclone.utils.PreferencesHelper.set
import com.mfo.instagramclone.utils.ex.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }


    private fun initUI() {
        initUISTate()
        initListeners()
    }

    private fun initUISTate() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.state.collect {
                    when(it) {
                        LoginState.Loading -> {}
                        is LoginState.Error -> errorState(it.error)
                        is LoginState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                if(email.trim().isNotEmpty() && password.trim().isNotEmpty()) {
                    val loginRequest = LoginRequest(email, password)

                    loginViewModel.authenticationUser(loginRequest)
                    hideKeyboard()
                    loadingState()
                }
            }
            btnCreateAccount.setOnClickListener {
                goToSignup()
            }
        }
    }

    private fun loadingState() {
        binding.apply {
            btnLogin.text = ""
            btnLogin.isEnabled = false
            pbLogin.isVisible = true
        }
    }

    private fun errorState(error: String) {
        AlertDialog.Builder(this)
            .setTitle("Incorrect Credentials")
            .setMessage(error)
            .setPositiveButton("ok", null)
            .show()
        binding.apply {
            pbLogin.isVisible = false
            btnLogin.text = getString(R.string.btn_login)
            btnLogin.isEnabled = true
        }
    }

    private fun successState(state: LoginState.Success) {
        createSessionPreferences(state.token)
        goToHome()
    }

    private fun createSessionPreferences(jwt: String) {
        val preferences = PreferencesHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToSignup() {
        /*val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()*/
    }
}