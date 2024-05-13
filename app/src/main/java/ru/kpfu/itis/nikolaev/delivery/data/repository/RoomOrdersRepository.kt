package ru.kpfu.itis.nikolaev.delivery.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.di.ServiceLocator
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignUpModel
/*

object RoomOrdersRepository {

    suspend fun insertData(orderModel: OrderModel) {
        with(orderModel) {
            ServiceLocator.database.dao.insertOrder(
                addressFrom, addressTo, price, dimensions, uidSender, uidRecipient, date
            )
        }
    }
    suspend fun fetchData() : Flow<List<OrderEntity>> {
        return ServiceLocator.database.dao.getAllOrders( )
    }

}*/
