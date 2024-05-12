package ru.kpfu.itis.nikolaev.delivery.data.repository

import ru.kpfu.itis.nikolaev.delivery.di.ServiceLocator
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignUpModel

object UsersRepository {

    suspend fun signUp(userSignUpModel: UserSignUpModel) {
        with(userSignUpModel) {
            ServiceLocator.database.dao.insertUser(
                role = role, name = name, second_name = secondName, email = email, password = password
            )
        }
    }

}