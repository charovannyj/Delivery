package ru.kpfu.itis.nikolaev.delivery.data.repository

import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel

interface OrdersRepository {
    suspend fun getOrders(uid: String, orderType :String) : List<OrderModel>

}