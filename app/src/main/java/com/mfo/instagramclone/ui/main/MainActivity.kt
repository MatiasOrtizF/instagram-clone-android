package com.mfo.instagramclone.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mfo.instagramclone.R
import com.mfo.instagramclone.databinding.ActivityMainBinding
import com.mfo.instagramclone.ui.login.LoginActivity
import com.mfo.instagramclone.utils.PreferencesHelper
import com.mfo.instagramclone.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        val preferences = PreferencesHelper.defaultPrefs(this)
        val token = preferences.getString("jwt", "").toString()
        mainViewModel.getUserInfo(token)
    }

    private fun initUI() {
        initUIState()
        initUIListeners()
        initNavigation()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when(it) {
                    MainState.Loading -> loadingState()
                    is MainState.Error -> errorState(it.error)
                    is MainState.Success -> successState(it)
                }
            }
        }
    }

    private fun initUIListeners() {
        /*binding.btnOutLog.setOnClickListener {
            clearSessionPreferences()
            goToLogin()
        }*/
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun loadingState() {
        //binding.pbMain.isVisible = true
    }

    private fun errorState(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        if(error == "Unauthorized: invalid token") {
            clearSessionPreferences()
            goToLogin()
        }
    }

    private fun successState(state: MainState.Success) {
        println(state.user)
        /*binding.pbMain.isVisible = false
        binding.llMain.isVisible = true*/
    }

    private fun clearSessionPreferences() {
        val preferences = PreferencesHelper.defaultPrefs(this)
        preferences["jwt"] = ""
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}