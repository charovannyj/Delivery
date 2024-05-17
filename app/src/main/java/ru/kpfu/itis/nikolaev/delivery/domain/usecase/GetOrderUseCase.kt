package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.data.repository.OrdersRepositoryImpl
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignInModel

class GetOrderUseCase (
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(uid: String, orderType :String): List<OrderModel> {
        var list : List<OrderModel>? = null
        withContext(dispatcher) {
            try {
                list = OrdersRepositoryImpl().getOrders(uid, orderType)

                true
            } catch (e: Exception) {
                false // Ошибка авторизации
            }
        }
        return list!!
    }
}