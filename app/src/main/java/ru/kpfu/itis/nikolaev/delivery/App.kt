package ru.kpfu.itis.nikolaev.delivery

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.yandex.mapkit.MapKitFactory
import com.yandex.maps.mobile.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import ru.kpfu.itis.nikolaev.delivery.di.ServiceLocator
import java.util.Properties

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.provideApplicationContext(this)

        MapKitFactory.setApiKey(ru.kpfu.itis.nikolaev.delivery.BuildConfig.MAPKIT_API_KEY)
        Firebase.initialize(this)

    }
}
