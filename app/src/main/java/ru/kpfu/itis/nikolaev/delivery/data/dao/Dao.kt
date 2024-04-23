package ru.kpfu.itis.nikolaev.delivery.data.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.nikolaev.delivery.data.entities.UserEntity

@Dao
interface Dao {
    @Query(
        "INSERT INTO users (role, name, second_name, email, password) " +
                "VALUES (:role, :name, :second_name, :email, :password)"
    )
    suspend fun insertUser(role: String, name: String, second_name: String, email: String, password: String)

    @Query("select * from users")
    fun getAllUsers(): Flow<List<UserEntity>>
}