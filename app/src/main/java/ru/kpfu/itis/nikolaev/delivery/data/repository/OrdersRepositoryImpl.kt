package ru.kpfu.itis.nikolaev.delivery.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import java.util.Date

class OrdersRepositoryImpl : OrdersRepository {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val dbInstance = FirebaseFirestore.getInstance()
    override fun getOrdersGet(): List<OrderModel> {
        val orders = mutableListOf<OrderModel>()
        dbInstance.document("clients/${uid}").collection("get").get()
            .addOnSuccessListener {
                it.forEach {
                    val addressFrom = it.getString("addressFrom") ?: ""
                    val addressTo = it.getString("addressTo") ?: ""
                    val date = it.getDate("date") ?: Date()
                    val dimensions = it.getString("dimensions") ?: ""
                    val price = it.getLong("price") ?: 0
                    val uidRecipient = it.getString("uidRecipient") ?: ""
                    val uidSender = it.getString("uidSender") ?: ""

                    // Создаем объект OrderModel и добавляем его в список
                    val order = OrderModel(
                        addressFrom,
                        addressTo,
                        price.toInt(),
                        dimensions,
                        uidSender,
                        uidRecipient,
                        date
                    )
                    orders.add(order)
                    Log.e("TAAAAAg", order.toString())                }

            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error getting documents: ", exception)
            }
        return orders
    }

    override fun getOrdersSend(): List<OrderModel> {
        val orders = mutableListOf<OrderModel>()
        dbInstance.document("clients/${uid}").collection("send").get()
            .addOnSuccessListener {
                it.forEach {
                    val addressFrom = it.getString("addressFrom") ?: ""
                    val addressTo = it.getString("addressTo") ?: ""
                    val date = it.getDate("date") ?: Date()
                    val dimensions = it.getString("dimensions") ?: ""
                    val price = it.getLong("price") ?: 0
                    val uidRecipient = it.getString("uidRecipient") ?: ""
                    val uidSender = it.getString("uidSender") ?: ""

                    // Создаем объект OrderModel и добавляем его в список
                    val order = OrderModel(
                        addressFrom,
                        addressTo,
                        price.toInt(),
                        dimensions,
                        uidSender,
                        uidRecipient,
                        date
                    )
                    orders.add(order)
                    Log.e("TAAAAAg", order.toString())                }

            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error getting documents: ", exception)
            }
        return orders
    }
}