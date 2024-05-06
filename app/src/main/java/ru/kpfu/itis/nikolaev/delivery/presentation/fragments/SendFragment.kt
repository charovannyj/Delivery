package ru.kpfu.itis.nikolaev.delivery.presentation.fragments


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Address
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error

import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentSendBinding
import ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels.RegistrationViewModel
import ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels.SendViewModel


class SendFragment : Fragment(R.layout.fragment_send) {
    private val viewModel: SendViewModel by viewModels()

    private val viewBinding: FragmentSendBinding by viewBinding(FragmentSendBinding::bind)
    private val startLocation = Point(55.792139, 49.122135)
    private val zoomValue = 15.0f
    private lateinit var mapView: MapView
    private lateinit var mapObjectCollection: MapObjectCollection
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                requestLocationAccess()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(requireContext())
        super.onCreate(savedInstanceState)


    }
    private val tapListener = GeoObjectTapListener {
            geoObjectTapEvent ->
        val selectionMetadata: GeoObjectSelectionMetadata = geoObjectTapEvent
            .geoObject
            .metadataContainer
            .getItem(GeoObjectSelectionMetadata::class.java)
        viewBinding.mapview.mapWindow.map.selectGeoObject(selectionMetadata)
        false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            observerData()
            mapObjectCollection = mapView.mapWindow.map.mapObjects
            val mapKit: MapKit = MapKitFactory.getInstance()
            val locationMapkit =
                mapKit.createUserLocationLayer(mapview.mapWindow) //вроде как геолокация пользоавтеля в реальном времени
            locationMapkit.isVisible = true
            mapview.mapWindow.map.addInputListener(inputListener) // Добавляем слушатель тапов по карте с извлечением информации
            mapView.mapWindow.map.addTapListener(tapListener)
            etFrom.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    submitQueryFrom(etFrom.text.toString())
                }
                false
            }
            etTo.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    submitQueryTo(etTo.text.toString())
                }
                false
            }
        }
    }



    private fun requestLocationAccess() {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }


    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    /*private fun observerData() {
        with(viewModel) {

            lifecycleScope.launch {
                selectedMarkerFromFlow.collect { result ->
                    result?.let {
                        viewBinding.etFrom.setText(result)
                    }
                }
            }
            lifecycleScope.launch {
                selectedMarkerToFlow.collect { result ->
                    result?.let {
                        viewBinding.etTo.setText(result)
                    }
                }
            }
        }
    }*/

    init {
        with(viewBinding){
            mapView = mapview
            requestLocationPermission()
            mapView.mapWindow.map.move(
                CameraPosition(startLocation, zoomValue, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 2f), null
            ) //мув к заранее выбранному месту
        }
        /*with(viewModel) {

            initStartAndFinishImages(requireContext())
            searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE) //важная вещь

        }*/
    }
    fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                0
            )
            return
        }
    }
    lateinit var searchManager: SearchManager
    lateinit var searchSession: Session
    fun submitQueryFrom(query: String) {
                searchSession = searchManager.submit(
                    query,
                    VisibleRegionUtils.toPolygon(mapView.mapWindow.map.visibleRegion),
                    SearchOptions(),
                    searchListenerFromTextToMapFrom
                )
    }

    fun submitQueryTo(query: String) {
                searchSession = searchManager.submit(
                    query,
                    VisibleRegionUtils.toPolygon(mapView.mapWindow.map.visibleRegion),
                    SearchOptions(),
                    searchListenerFromTextToMapTo
                )
        }
    }

    private val searchListenerFromTextToMapFrom = object : Session.SearchListener {

        override fun onSearchResponse(response: Response) {
                    for (searchResult in response.collection.children) {
                        val resultLocation = searchResult.obj!!.geometry[0].point
                        if (resultLocation != null) {
                            if (startMarker != null) {
                                removeMarker(startMarker!!)
                            }
                            startMarker = mapObjectCollection.addPlacemark(
                                resultLocation,
                                ImageProvider.fromBitmap(markerStart)
                            )

                        }
                    }
            }

        override fun onSearchError(p0: Error) {
            TODO("Not yet implemented")
        }
    }

        override fun onSearchError(p0: Error) {
            TODO("Not yet implemented")
        }
    }
    private val searchListenerFromTextToMapTo = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            viewModelScope.launch {
                try {
                    for (searchResult in response.collection.children) {
                        val resultLocation = searchResult.obj!!.geometry[0].point
                        if (resultLocation != null) {
                            if (finishMarker != null) {
                                removeMarker(finishMarker!!)
                            }
                            finishMarker = mapObjectCollection.addPlacemark(
                                resultLocation,
                                ImageProvider.fromBitmap(markerFinish)
                            )
                        }
                    }
                } catch (e: Exception) {

                }
            }

        }

        override fun onSearchError(p0: Error) {
            TODO("Not yet implemented")
        }
    }

    //безумно важная вещь (метку мапает в текст в et)
    public val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            searchSession = searchManager.submit(point, 20, SearchOptions(), searchListenerEtFrom)
            //отправлять поинт на бек дял измерения расстояния

            if (startMarker != null) {
                removeMarker(startMarker!!)
            }
            startMarker =
                mapObjectCollection.addPlacemark(point, ImageProvider.fromBitmap(markerStart))

        }

        override fun onMapLongTap(map: Map, point: Point) {
            searchSession = searchManager.submit(point, 20, SearchOptions(), searchListenerEtTo)

            if (finishMarker != null) {
                removeMarker(finishMarker!!)
            }
            finishMarker =
                mapObjectCollection.addPlacemark(point, ImageProvider.fromBitmap(markerFinish))
            //отправлять поинт на бек для измерения расстояния

        }
    }
}