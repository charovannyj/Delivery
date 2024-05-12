package ru.kpfu.itis.nikolaev.delivery.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kpfu.itis.nikolaev.delivery.data.dao.Dao
import ru.kpfu.itis.nikolaev.delivery.data.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class MainDb : RoomDatabase(){
    abstract val dao: Dao
    companion object {
        const val DATABASE_NAME = "delivery"
    }
}