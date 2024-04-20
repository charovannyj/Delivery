package ru.kpfu.itis.nikolaev.delivery.di

import android.content.Context
import androidx.room.Room
import ru.kpfu.itis.nikolaev.delivery.data.database.MainDb

object ServiceLocator {

    private const val SHARED_PREFERENCE_NAME = "user_session_pref"

    val database: MainDb by lazy {
        val context = applicationContext ?: throw Exception()
        Room.databaseBuilder(
            context, MainDb::class.java, "est.db"
        ).build()
    }


    private var applicationContext: Context? = null

    fun provideApplicationContext(context: Context) {
        applicationContext = context
    }

}
