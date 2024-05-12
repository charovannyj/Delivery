package ru.kpfu.itis.nikolaev.delivery.data.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.nikolaev.delivery.data.entities.OrderEntity
import ru.kpfu.itis.nikolaev.delivery.data.entities.UserEntity
import java.util.Date

@Dao
interface Dao {
    @Query(
        "INSERT INTO users (role, name, second_name, email, password) " +
                "VALUES (:role, :name, :second_name, :email, :password)"
    )
    suspend fun insertUser(role: String, name: String, second_name: String, email: String, password: String)

    @Query("select * from users")
    fun getAllUsers(): Flow<List<UserEntity>>
    @Query(
        "INSERT INTO orders (address_from, address_to, price, dimensions, uid_sender, uid_recipient, date) " +
                "VALUES (:addressFrom, :addressTo, :price, :dimensions, :uidSender, :uidRecipient, :date)"
    )
    suspend fun insertOrder( addressFrom: String,
                             addressTo: String,
                             price: Int,
                             dimensions: String,
                             uidSender: String,
                             uidRecipient: String,
                             date: Date
    )

    @Query("select * from orders")
    fun getAllOrders(): Flow<List<OrderEntity>>
}