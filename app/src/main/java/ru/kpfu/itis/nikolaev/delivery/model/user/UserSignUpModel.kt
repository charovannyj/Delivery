package ru.kpfu.itis.nikolaev.delivery.model.user

data class UserSignUpModel(
    val name: String,
    val secondName: String,
    val email: String,
    val password: String
)