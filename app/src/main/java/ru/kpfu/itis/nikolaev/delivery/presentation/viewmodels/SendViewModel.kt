package ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Address
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.CreateBitmapFromVectorUseCase
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.RemoveMarkerUseCase

class SendViewModel : ViewModel() {
    private var _selectedBlueObjectFlow = MutableSharedFlow<Boolean>(1)
    val selectedBlueObjectFlow: SharedFlow<Boolean?>
        get() = _selectedBlueObjectFlow


    private var _selectedMarkerFromFlow = MutableSharedFlow<String>(1)
    val selectedMarkerFromFlow: SharedFlow<String?>
        get() = _selectedMarkerFromFlow


    private var _selectedMarkerToFlow = MutableSharedFlow<String>(1)
    val selectedMarkerToFlow: SharedFlow<String?>
        get() = _selectedMarkerToFlow


    private val startLocation = Point(55.792139, 49.122135)
    private var startMarker: PlacemarkMapObject? = null
    private var finishMarker: PlacemarkMapObject? = null
    lateinit var searchManager: SearchManager
    lateinit var searchSession: Session
    lateinit var markerFinish: Bitmap
    lateinit var markerStart: Bitmap
    private val zoomValue = 15.0f
    private lateinit var mapView: MapView
    private lateinit var mapObjectCollection: MapObjectCollection

    fun removeMarker(marker: PlacemarkMapObject) {
        viewModelScope.launch {
            try {
                RemoveMarkerUseCase(Dispatchers.IO, mapObjectCollection).invoke(marker)
            } catch (_: Exception) {
            }
        }
    }
    fun initStartAndFinishImages(ctx: Context) {
        viewModelScope.launch {
            try {
                markerStart = createBitmapFromVector(
                    R.drawable.location_map_pin_mark_icon_148684,
                    ctx
                )
                markerFinish = createBitmapFromVector(
                    R.drawable.finish_er83q5mw52un,
                    ctx
                )
            } catch (e: Exception) {
            }
        }
    }

    fun createBitmapFromVector(art: Int, ctx: Context): Bitmap {
        lateinit var result: Bitmap

        viewModelScope.launch {
            try {
                result = CreateBitmapFromVectorUseCase(Dispatchers.IO).invoke(art, ctx)!!
            } catch (e: Exception) {

            }
        }
        return result
    }


    private fun getAddressFromMarker(response: Response, flow: MutableSharedFlow<String>) {
        viewModelScope.launch {
            try {
                val addressComponents = response.collection.children.firstOrNull()?.obj
                    ?.metadataContainer
                    ?.getItem(ToponymObjectMetadata::class.java)
                    ?.address
                    ?.components
                if (addressComponents != null) {
                    val city =
                        addressComponents.firstOrNull { it.kinds.contains(Address.Component.Kind.LOCALITY) }?.name
                            ?: "Город не найден"
                    val street =
                        addressComponents.firstOrNull { it.kinds.contains(Address.Component.Kind.STREET) }?.name
                            ?: "Улица не найдена"
                    val houseNumber =
                        addressComponents.firstOrNull { it.kinds.contains(Address.Component.Kind.HOUSE) }?.name
                            ?: "Номер дома не найден"

                    val addressText = "$city, $street, $houseNumber"
                    flow.emit(addressText)
                } else {

                }
            } catch (_: Exception) {

            }
        }


    }

    //самая полезная вещь, показывает адрес в тосте
    private val searchListenerEtFrom = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            getAddressFromMarker(response, _selectedMarkerFromFlow)
        }

        override fun onSearchError(p0: Error) {
            TODO("Not yet implemented")
        }
    }

    private val searchListenerEtTo = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            getAddressFromMarker(response, _selectedMarkerToFlow)
        }

        override fun onSearchError(p0: Error) {
            TODO("Not yet implemented")
        }
    }
}