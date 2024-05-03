package ru.kpfu.itis.nikolaev.delivery

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import com.yandex.maps.mobile.BuildConfig
import ru.kpfu.itis.nikolaev.delivery.di.ServiceLocator
import java.util.Properties

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.provideApplicationContext(this)

    }
}
