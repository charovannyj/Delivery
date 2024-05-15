package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.data.repository.OrdersRepositoryImpl

class FetchProfileDataUseCase (
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(uid: String, orderType :String): List<OrderModel> {
        var list : List<OrderModel>? = null
        withContext(dispatcher) {
            try {
                Log.e("TAAAAAAAAG", "3")

                list = OrdersRepositoryImpl().getOrders(uid, orderType)
                Log.e("TAAAAAAAAG", "4")
                true
            } catch (e: Exception) {
                false // Ошибка авторизации
            }
        }
        return list!!
    }
}