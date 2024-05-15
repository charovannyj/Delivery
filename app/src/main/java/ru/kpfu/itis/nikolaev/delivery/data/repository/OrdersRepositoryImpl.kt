package ru.kpfu.itis.nikolaev.delivery.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import java.util.Calendar
import java.util.Date

class OrdersRepositoryImpl : OrdersRepository {
    val dbInstance = FirebaseFirestore.getInstance()
    override suspend fun getOrders(uid: String, orderType: String): List<OrderModel> = coroutineScope {
        val orders = mutableListOf<OrderModel>()
        try {
            val result = dbInstance.document("clients/${uid}").collection(orderType).get().await()
            for (it in result) {
                    val addressFrom = it.get("addressFrom") ?: ""
                    val addressTo = it.get("addressTo") ?: ""
                    val date = it.getDate("date") ?: Date() //  Если date null, то используем текущую дату
                    val calendar = Calendar.getInstance()
                    calendar.time = date // Устанавливаем дату в Calendar
                    val dimensions = it.get("dimensions") ?: ""
                    val price = it.getLong("price") ?: 0
                    val uidRecipient = it.get("uidRecipient") ?: ""
                    val uidSender = it.get("uidSender") ?: ""
                    Log.e("it", it.toString())
                    val order = OrderModel(
                        addressFrom.toString(),
                        addressTo.toString(),
                        price.toInt(),
                        dimensions.toString(),
                        uidSender.toString(),
                        uidRecipient.toString(),
                        calendar
                    )
                    Log.e("ordermodel", order.toString())
                    orders.add(order)
            }
        } catch (exception: Exception) {
            // Обработка ошибок
        }
        orders
    }

}