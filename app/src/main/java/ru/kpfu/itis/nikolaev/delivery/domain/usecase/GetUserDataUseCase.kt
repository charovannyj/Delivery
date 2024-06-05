package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.data.repository.OrdersRepositoryImpl
import ru.kpfu.itis.nikolaev.delivery.data.repository.UserRepositoryImpl
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserModel

class GetUserDataUseCase (
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(uid: String): UserModel {
        lateinit var user : UserModel
        withContext(dispatcher) {
            try {
                user = UserRepositoryImpl().getUser(uid)

                true
            } catch (e: Exception) {
                false
            }
        }
        return user!!
    }
}