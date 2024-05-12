package ru.kpfu.itis.nikolaev.delivery.data.repository

import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel

interface OrdersRepository {
    fun getOrdersGet() : List<OrderModel>
    fun getOrdersSend() : List<OrderModel>

}