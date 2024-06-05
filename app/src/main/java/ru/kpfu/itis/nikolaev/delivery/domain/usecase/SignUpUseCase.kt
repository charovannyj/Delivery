package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignUpModel
import ru.kpfu.itis.nikolaev.delivery.utils.Hasher
import java.security.MessageDigest

class SignUpUseCase(
    private val dispatcher: CoroutineDispatcher,
) {
    val auth = FirebaseAuth.getInstance()
    val dbInstance =  FirebaseFirestore.getInstance()

    suspend operator fun invoke(user: UserSignUpModel): Boolean {
        return withContext(dispatcher) {
            try {
                auth.createUserWithEmailAndPassword(user.email, user.password).await()
                saveUser(user)
                true // Успешная авторизация
            } catch (e: Exception) {
                false // Ошибка авторизации
            }
        }
    }
    private fun saveUser(user: UserSignUpModel){
        val map: MutableMap<String, Any> = HashMap()
        val passwordHash = user.password.hashedWithSha256()
        map["name"] = user.name
        map["secondName"] = user.secondName
        map["email"] = user.email
        map["role"] = user.role
        map["password"] = passwordHash

        dbInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid.toString()}").set(map)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG",
                    "DocumentSnapshot added with ID: "
                )
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) } //заполнение данными

    }
    @OptIn(ExperimentalStdlibApi::class)
    fun String.hashedWithSha256() =
        MessageDigest.getInstance("SHA-256")
            .digest(toByteArray())
            .toHexString()

}