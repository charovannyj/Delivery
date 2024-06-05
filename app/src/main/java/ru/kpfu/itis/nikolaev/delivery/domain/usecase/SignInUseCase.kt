package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
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
                val result = auth.signInWithEmailAndPassword(user.email, user.password)
                    .addOnSuccessListener {
                        Log.e("a", "kruto")
                    }
                    .wait()
                Log.e("a", "done")
                result.isSuccessful // Успешная авторизация
            } catch (e: Exception) {
                false // Ошибка авторизации
            }
        }
    }

    suspend fun <T> Task<T>.wait(): Task<T> {
        while (true) {
            if (isComplete) break
            else delay(100L)
        }
        return this
    }
}