package ru.kpfu.itis.nikolaev.delivery.domain.model

data class UserSignUpModel(
    val role : String,
    val name: String,
    val secondName: String,
    val email: String,
    val password: String
)