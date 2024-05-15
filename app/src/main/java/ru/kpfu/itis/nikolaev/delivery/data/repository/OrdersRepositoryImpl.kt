package ru.kpfu.itis.nikolaev.delivery.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.utils.ConvertDate
import java.util.Calendar
import java.util.Date

class OrdersRepositoryImpl : OrdersRepository {
    val dbInstance = FirebaseFirestore.getInstance()
    override suspend fun getOrders(uid: String, orderType: String): List<OrderModel> = coroutineScope {
        val orders = mutableListOf<OrderModel>()
        try {
            val result = dbInstance.document("clients/${uid}").collection(orderType).get().await()
            for (it in result) {
                Log.e("TAAAAAAAAG", "мы в OrdersRepositoryImpl")

                val addressFrom = it.get("addressFrom") ?: ""
                Log.e("TAAAAAAAAG", "address ${addressFrom}")
                val addressTo = it.get("addressTo") ?: ""
                val date = it.get("date") ?: "" //  Если date null, то используем текущую дату
                Log.e("TAAAAAAAAG", "date ${date}")


                val dimensions = it.get("dimensions") ?: ""
                Log.e("TAAAAAAAAG", "dimensions ${dimensions}")

                val price = it.getLong("price") ?: 0
                val uidRecipient = it.get("uidRecipient") ?: ""
                val uidSender = it.get("uidSender") ?: ""
                val status = it.get("status") ?: ""
                Log.e("it", it.toString())
                val order = OrderModel(
                    addressFrom.toString(),
                    addressTo.toString(),
                    price.toInt(),
                    dimensions.toString(),
                    uidSender.toString(),
                    uidRecipient.toString(),
                    ConvertDate.convertSimpleDateToFull(date.toString()),
                    status.toString()
                )
                Log.e("TAAAAAAAAG", order.toString())
                orders.add(order)
            }
        } catch (exception: Exception) {
            // Обработка ошибок
        }
        orders
    }

}