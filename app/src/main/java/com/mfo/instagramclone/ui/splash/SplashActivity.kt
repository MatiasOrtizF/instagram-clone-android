package com.mfo.instagramclone.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mfo.instagramclone.ui.login.LoginActivity
import com.mfo.instagramclone.ui.main.MainActivity
import com.mfo.instagramclone.ui.main.MainState
import com.mfo.instagramclone.utils.PreferencesHelper
import com.mfo.instagramclone.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashViewModel.getUserInfo(getToken())
        initUIState()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            splashViewModel.state.collect {
                when(it) {
                    is SplashState.Success -> successState(it.user.imageProfile)
                    is SplashState.Error -> errorState(it.error)
                    else -> Unit
                }
            }
        }
    }

    private fun successState(imageProfile: String?) {
        handleGoToMain(imageProfile)
    }

    private fun errorState(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        if(error == "Unauthorized: invalid token") {
            clearSessionPreferences()
            handleGoToLogin()
        } else {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getToken(): String {
        val preferences = PreferencesHelper.defaultPrefs(this)
        return preferences.getString("jwt", "").toString()
    }

    private fun clearSessionPreferences() {
        val preferences = PreferencesHelper.defaultPrefs(this)
        preferences["jwt"] = ""
    }

    private fun handleGoToMain(imageProfile: String?) {
        val intent = Intent(this, MainActivity::class.java).putExtra("imageProfile", imageProfile)
        startActivity(intent)
        finish()
    }

    private fun handleGoToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}