package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoveMarkerUseCase (
    private val dispatcher: CoroutineDispatcher,
    private val mapObjectCollection: MapObjectCollection

){
    suspend operator fun invoke(marker : PlacemarkMapObject){
        withContext(dispatcher) {
            marker.let {
                mapObjectCollection.remove(it)
            }
        }

    }
}