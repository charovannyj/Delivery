package ru.kpfu.itis.nikolaev.delivery.domain.model

data class UserModel(
    val id: Int,
    val role : String,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
)