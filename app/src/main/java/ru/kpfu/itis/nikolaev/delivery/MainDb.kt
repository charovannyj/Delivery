package ru.kpfu.itis.nikolaev.delivery

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kpfu.itis.nikolaev.delivery.data.db.dao.Dao
import ru.kpfu.itis.nikolaev.delivery.data.db.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class MainDb : RoomDatabase(){
    abstract fun getDao() : Dao
    companion object{
        fun getDb(context: Context?) : MainDb{
            return Room.databaseBuilder(
                context!!.applicationContext,
                MainDb::class.java,
                "test.db"
            ).build()
        }
    }
}