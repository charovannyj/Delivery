package ru.kpfu.itis.nikolaev.delivery.data.repository

import ru.kpfu.itis.nikolaev.delivery.di.ServiceLocator
import ru.kpfu.itis.nikolaev.delivery.model.user.UserSignUpModel

object UsersRepository {

    suspend fun signUp(userSignUpModel: UserSignUpModel) {
        with(userSignUpModel) {
            ServiceLocator.database.userDao.insertUser(
                name = name, second_name = secondName, email = email, password = password
            )
        }
    }



}