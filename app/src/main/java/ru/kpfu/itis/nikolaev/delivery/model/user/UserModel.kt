package ru.kpfu.itis.nikolaev.delivery.model.user

data class UserModel(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
)