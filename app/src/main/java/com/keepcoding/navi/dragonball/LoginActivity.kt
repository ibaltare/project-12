package com.keepcoding.navi.dragonball

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.keepcoding.navi.dragonball.models.MainActivityState
import com.keepcoding.navi.dragonball.viewModels.LoginViewModel
import com.keepcoding.navi.dragonball.databinding.ActivityMainBinding
import com.keepcoding.navi.dragonball.models.Constants
import com.keepcoding.navi.dragonball.models.SharedPreferences

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        with(binding){
            btnLoginEnabled()
            editTextEmailAddress.doAfterTextChanged {
                btnLoginEnabled()
            }
            editTextPassword.doAfterTextChanged {
                btnLoginEnabled()
            }
            btnLogin.setOnClickListener {
                viewModel.doLogin(user = editTextEmailAddress.text.toString(), pass = editTextPassword.text.toString())
            }
        }
    }

    private fun setObservers() {
        viewModel.stateLiveData.observe(this) {
            when (it) {
                is MainActivityState.Loading -> {
                    btnLoginState( false)
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MainActivityState.Error -> {
                    showMessage(it.message)
                    btnLoginState(true)
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is MainActivityState.Success -> {
                    it.message?.let { token ->
                        SharedPreferences.saveToken(token,this@LoginActivity)
                    }
                    btnLoginState(true)
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun btnLoginEnabled(){
        btnLoginState(binding.editTextEmailAddress.text.isNotEmpty() && binding.editTextPassword.text.isNotEmpty())
    }

    private fun btnLoginState(state : Boolean) {
        binding.btnLogin.isEnabled = state
    }

    private fun showMessage(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}