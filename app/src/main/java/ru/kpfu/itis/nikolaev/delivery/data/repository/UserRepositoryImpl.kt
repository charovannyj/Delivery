package ru.kpfu.itis.nikolaev.delivery.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserModel
import ru.kpfu.itis.nikolaev.delivery.utils.ConvertDate

class UserRepositoryImpl {
    val dbInstance = FirebaseFirestore.getInstance()
    suspend fun getUser(uid: String): UserModel {
        return withContext(Dispatchers.IO) {
            lateinit var user : UserModel
            try {
                val result = dbInstance.document("users/${uid}").get().await().data
                result?.let {
                    val email = it["email"] ?: ""
                    val name = it["name"] ?: ""
                    val password = it["password"] ?: ""
                    val role = it["role"] ?: ""
                    val secondName = it["secondName"] ?: 0
                    val userModel = UserModel(
                        role.toString(),
                        name.toString(),
                        secondName.toString(),
                        email.toString(),
                        password.toString()
                    )
                    user = userModel
                }
            } catch (exception: Exception) {

            }
            user
        }

    }
    /*suspend fun getName(): String {
        return getUser(Firebase.auth.currentUser?.uid.toString()).name
    }*/

}