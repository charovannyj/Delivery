package ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels

import android.os.Build
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.Address
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.SendOrderUseCase
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.SignUpUseCase
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

class SendViewModel : ViewModel() {
    private var _fromTextToMapFromFlow = MutableStateFlow(Unit)
    val fromTextToMapFromFlow: StateFlow<Unit>
        get() = _fromTextToMapFromFlow
    private var _sendOrderFlow = MutableStateFlow("")
    val sendOrderFlow: StateFlow<String>
        get() = _sendOrderFlow

    private val searchManager = SearchFactory.getInstance()
        .createSearchManager(SearchManagerType.ONLINE) //важная вещь
    private var searchSession: Session? = null
    private var pointFrom : Point? = null
    private var pointTo : Point? = null
    fun submitQueryFrom(query: String, visibleRegion: VisibleRegion) {
        searchSession = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(visibleRegion),
            SearchOptions().apply {
                SearchType.NONE
                disableSpellingCorrection
                resultPageSize = 32
            },
            searchListenerFromTextToMapFrom
        )
    }
    fun submitQueryTo(query: String, visibleRegion: VisibleRegion) {
        searchSession = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(visibleRegion),
            SearchOptions().apply {
                SearchType.NONE
                disableSpellingCorrection
                resultPageSize = 32
            },
            searchListenerFromTextToMapTo
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendOrder(orderModel: OrderModel){
        viewModelScope.launch {
            try {
                val result = SendOrderUseCase(Dispatchers.IO).invoke(orderModel)
                _sendOrderFlow.emit(result.toString())
            } catch (e: Exception) {
                Log.e("TAG_error", "error")
            }
        }
    }
    fun onMapTap(point: Point, onResponse: (Response) -> Unit) {
        searchSession =
            searchManager.submit(point, 20, SearchOptions(), object : Session.SearchListener {
                override fun onSearchResponse(response: Response) {
                    //markerToAddress(response, viewBinding.etFrom)
                    onResponse(response)
                }

                override fun onSearchError(p0: Error) {

                }
            })
    }

    fun onMapLongTap(point: Point, onResponse: (Response) -> Unit) {
        searchSession =
            searchManager.submit(point, 20, SearchOptions(), object : Session.SearchListener {
                override fun onSearchResponse(response: Response) {
                    //markerToAddress(response, viewBinding.etTo)
                    onResponse(response)

                }

                override fun onSearchError(p0: Error) {
                }
            }
            )

    }
    fun getDistance(): Int {
        val earthRadius = 6371 // Earth radius in kilometers
        val lat1 = Math.toRadians(pointFrom!!.latitude)
        val lon1 = Math.toRadians(pointFrom!!.longitude)
        val lat2 = Math.toRadians(pointTo!!.latitude)
        val lon2 = Math.toRadians(pointTo!!.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2) * sin(dLat / 2) + cos(lat1) * cos(lat2) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return round(earthRadius * c).toInt()
    }



    private val searchListenerFromTextToMapFrom = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            for (searchResult in response.collection.children) {
                pointFrom = searchResult.obj!!.geometry[0].point
            }
            viewModelScope.launch {
                _fromTextToMapFromFlow.emit(Unit)
            }
            searchCallbackListenerFrom?.invoke(response)
        }

        override fun onSearchError(p0: Error) {
        }
    }
    private val searchListenerFromTextToMapTo = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            for (searchResult in response.collection.children) {
                pointTo = searchResult.obj!!.geometry[0].point
            }

            viewModelScope.launch {
                //_fromTextToMapToFlow.emit(Unit)
            }
            searchCallbackListenerTo?.invoke(response)
        }

        override fun onSearchError(p0: Error) {
        }
    }


    private var searchCallbackListenerFrom: ((Response) -> Unit)? = null
    fun setSearchCallbackListenerFrom(callback: (Response) -> Unit) {
        searchCallbackListenerFrom = callback
    }


    private var searchCallbackListenerTo: ((Response) -> Unit)? = null
    fun setSearchCallbackListenerTo(callback: (Response) -> Unit) {
        searchCallbackListenerTo = callback
    }
    fun sendPointFromInfo(point: Point) {
        pointFrom = point
    }

    fun sendPointToInfo(point: Point) {
        pointTo = point
    }
}