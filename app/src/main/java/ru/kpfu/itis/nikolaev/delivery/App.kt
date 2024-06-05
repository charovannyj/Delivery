package ru.kpfu.itis.nikolaev.delivery

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import ru.kpfu.itis.nikolaev.delivery.di.ServiceLocator

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.provideApplicationContext(this)

        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        Firebase.initialize(this)

    }
}
