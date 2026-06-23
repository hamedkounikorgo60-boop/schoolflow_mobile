package com.schoolflow.mobile.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.schoolflow.mobile.R
import com.schoolflow.mobile.databinding.ActivityForgotPasswordBinding
import com.schoolflow.mobile.utils.Resource
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnSend.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (validateInput(email)) {
                viewModel.forgotPassword(email)
            }
        }

        binding.btnBackToLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(email: String): Boolean {
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.field_required)
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.invalid_email)
            return false
        }
        binding.tilEmail.error = null
        return true
    }

    private fun observeViewModel() {
        viewModel.forgotPasswordResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.btnSend.isEnabled = false
                    binding.tvError.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.btnSend.isEnabled = true
                    Toast.makeText(this, R.string.forgot_password_success, Toast.LENGTH_LONG).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.btnSend.isEnabled = true
                    binding.tvError.text = result.message
                    binding.tvError.visible()
                }
            }
        }
    }
}
