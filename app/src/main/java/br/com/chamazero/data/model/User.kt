package br.com.chamazero.data.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val initials: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: User
)

data class UpdateProfileRequest(
    val name: String,
    val email: String
)
