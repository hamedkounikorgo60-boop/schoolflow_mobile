package com.schoolflow.mobile.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.schoolflow.mobile.R
import com.schoolflow.mobile.databinding.ActivityChangePasswordBinding
import com.schoolflow.mobile.utils.Resource
import com.schoolflow.mobile.utils.gone
import com.schoolflow.mobile.utils.visible

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnChangePassword.setOnClickListener {
            val currentPassword = binding.etCurrentPassword.text.toString().trim()
            val newPassword = binding.etNewPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateInput(currentPassword, newPassword, confirmPassword)) {
                viewModel.changePassword(currentPassword, newPassword, confirmPassword)
            }
        }
    }

    private fun validateInput(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (currentPassword.isEmpty()) {
            binding.tilCurrentPassword.error = getString(R.string.field_required)
            isValid = false
        } else {
            binding.tilCurrentPassword.error = null
        }

        if (newPassword.isEmpty()) {
            binding.tilNewPassword.error = getString(R.string.field_required)
            isValid = false
        } else if (newPassword.length < 8) {
            binding.tilNewPassword.error = getString(R.string.password_too_short)
            isValid = false
        } else {
            binding.tilNewPassword.error = null
        }

        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = getString(R.string.field_required)
            isValid = false
        } else if (confirmPassword != newPassword) {
            binding.tilConfirmPassword.error = getString(R.string.password_mismatch)
            isValid = false
        } else {
            binding.tilConfirmPassword.error = null
        }

        return isValid
    }

    private fun observeViewModel() {
        viewModel.changePasswordResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.btnChangePassword.isEnabled = false
                    binding.tvError.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.btnChangePassword.isEnabled = true
                    Toast.makeText(this, R.string.password_changed_success, Toast.LENGTH_LONG).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.btnChangePassword.isEnabled = true
                    binding.tvError.text = result.message
                    binding.tvError.visible()
                }
            }
        }
    }
}
