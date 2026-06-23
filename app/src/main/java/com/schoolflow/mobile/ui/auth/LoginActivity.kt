package com.schoolflow.mobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.schoolflow.mobile.databinding.ActivityLoginBinding
import com.schoolflow.mobile.ui.MainActivity
import com.schoolflow.mobile.utils.Resource
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible
import com.schoolflow.mobile.SchoolFlowApp

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (SchoolFlowApp.instance.sessionManager.isLoggedIn()) {
            navigateToMain()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                viewModel.login(email, password)
            }
        }

        binding.btnForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.tilEmail.error = getString(com.schoolflow.mobile.R.string.field_required)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(com.schoolflow.mobile.R.string.invalid_email)
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = getString(com.schoolflow.mobile.R.string.field_required)
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        return isValid
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.btnLogin.isEnabled = false
                    binding.tvError.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.btnLogin.isEnabled = true
                    result.data?.let { loginResponse ->
                        SchoolFlowApp.instance.sessionManager.saveSession(
                            loginResponse.token,
                            loginResponse.user,
                            loginResponse.eleves
                        )
                        navigateToMain()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.btnLogin.isEnabled = true
                    binding.tvError.text = result.message
                    binding.tvError.visible()
                }
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
