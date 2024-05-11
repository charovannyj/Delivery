package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignUpModel

class SignUpUseCase(
    private val dispatcher: CoroutineDispatcher,
) {
    val auth = FirebaseAuth.getInstance()

    suspend operator fun invoke(user: UserSignUpModel): Boolean {
        return withContext(dispatcher) {
            try {
                auth.createUserWithEmailAndPassword(user.email, user.password).await()
                true // Успешная авторизация
            } catch (e: Exception) {
                false // Ошибка авторизации
            }
        }
    }
}