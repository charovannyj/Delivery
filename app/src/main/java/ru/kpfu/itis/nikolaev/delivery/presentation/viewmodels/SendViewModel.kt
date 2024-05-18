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

    fun submitQueryFrom(query: String, visibleRegion: VisibleRegion) {
        searchSession = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(visibleRegion),
            SearchOptions(),
            searchListenerFromTextToMapFrom
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

    fun submitQueryTo(query: String, visibleRegion: VisibleRegion) {
        searchSession = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(visibleRegion),
            SearchOptions(),
            searchListenerFromTextToMapTo
        )
    }

    private val searchListenerFromTextToMapFrom = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
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
            viewModelScope.launch {

                //_fromTextToMapFromFlow.emit(Unit)

            }
            searchCallbackListenerTo?.invoke(response)
        }

        override fun onSearchError(p0: Error) {
        }
    }


    private var searchCallbackListenerFrom: ((Response) -> Unit)? = null
    public fun setSearchCallbackListenerFrom(callback: (Response) -> Unit) {
        searchCallbackListenerFrom = callback
    }


    private var searchCallbackListenerTo: ((Response) -> Unit)? = null
    public fun setSearchCallbackListenerTo(callback: (Response) -> Unit) {
        searchCallbackListenerTo = callback
    }


}