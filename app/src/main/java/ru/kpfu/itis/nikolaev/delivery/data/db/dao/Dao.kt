package ru.kpfu.itis.nikolaev.delivery.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.nikolaev.delivery.data.db.entity.UserEntity

@Dao
interface Dao {
    @Insert
    fun insertUser(user : UserEntity)
    @Query("select * from users")
    fun getAllUsers() : Flow<List<UserEntity>>
}