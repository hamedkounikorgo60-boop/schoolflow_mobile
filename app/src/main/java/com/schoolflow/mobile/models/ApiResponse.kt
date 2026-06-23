package com.schoolflow.mobile.models

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)

data class LoginRequest(
    val email: String,
    @SerializedName("password") val motDePasse: String
)

data class LoginResponse(
    val token: String,
    val user: User,
    val eleves: List<Eleve>
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String? = null
)

data class ForgotPasswordRequest(
    val email: String
)

data class ChangePasswordRequest(
    @SerializedName("current_password") val currentPassword: String,
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("new_password_confirmation") val newPasswordConfirmation: String
)

data class MoyenneTrimestrielle(
    val trimestre: Int,
    val moyenne: Double,
    val rang: Int? = null,
    val mention: String? = null
) {
    val trimestreLabel: String
        get() = when (trimestre) {
            1 -> "1er Trimestre"
            2 -> "2ème Trimestre"
            3 -> "3ème Trimestre"
            else -> "Trimestre $trimestre"
        }
}
