package ru.kpfu.itis.nikolaev.delivery

import android.app.Application
import ru.kpfu.itis.nikolaev.delivery.di.ServiceLocator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.provideApplicationContext(this)
    }
}
