package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignInModel

class SignInUseCase(
    private val dispatcher: CoroutineDispatcher,
) {
    val auth = FirebaseAuth.getInstance()

    suspend operator fun invoke(user: UserSignInModel): Boolean {
        return withContext(dispatcher) {
            try {
                auth.signInWithEmailAndPassword(user.email, user.password).await()
                true // Успешная авторизация
            } catch (e: Exception) {
                false // Ошибка авторизации
            }
        }
    }
}