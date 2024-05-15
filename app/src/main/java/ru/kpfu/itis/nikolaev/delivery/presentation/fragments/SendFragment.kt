package ru.kpfu.itis.nikolaev.delivery.presentation.fragments


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
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
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
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Address
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error

import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentSendBinding
import ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels.SendViewModel
import java.util.Date


class SendFragment : Fragment(R.layout.fragment_send) {
    private val viewBinding: FragmentSendBinding by viewBinding(FragmentSendBinding::bind)
    private val viewModel: SendViewModel by viewModels()

    private val startLocation = Point(55.792139, 49.122135)
    lateinit var markerFinish: Bitmap
    lateinit var markerStart: Bitmap
    private var startMarker: PlacemarkMapObject? = null
    private var finishMarker: PlacemarkMapObject? = null
    private val zoomValue = 15.0f
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                requestLocationAccess()
            }
        }
    private lateinit var mapView: MapView
    private lateinit var mapObjectCollection: MapObjectCollection

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerData()

        with(viewBinding) {

            mapView = mapview
            markerStart = createBitmapFromVector(R.drawable.location_map_pin_mark_icon_148684)!!
            markerFinish = createBitmapFromVector(R.drawable.finish_er83q5mw52un)!!

            mapObjectCollection = mapView.mapWindow.map.mapObjects

            mapView.mapWindow.map.addTapListener(tapListener)     //выделение домика
            mapView.mapWindow.map.move(
                CameraPosition(startLocation, zoomValue, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 2f), null
            ) //мув к заранее выбранному месту
            val mapKit: MapKit = MapKitFactory.getInstance()
            requestLocationPermission()

            val locationMapkit =
                mapKit.createUserLocationLayer(mapview.mapWindow) //вроде как геолокация пользоавтеля в реальном времени
            locationMapkit.isVisible = true

            mapview.mapWindow.map.addInputListener(touchListener) // Добавляем слушатель тапов по карте с извлечением информации

            mapObjectCollection = mapView.mapWindow.map.mapObjects
            etFrom.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.submitQueryFrom(
                        etFrom.text.toString(),
                        mapView.mapWindow.map.visibleRegion
                    )
                }
                false
            }
            etTo.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.submitQueryTo(
                        etTo.text.toString(),
                        mapView.mapWindow.map.visibleRegion
                    )
                }
                false
            }
            btnSend.setOnClickListener{
                val addressFrom = etFrom.text.toString()
                val addressTo = etTo.text.toString()
                val price = etPrice.text.toString().toInt()
                val dimensions = etDimensions.text.toString()
                val uidSender = FirebaseAuth.getInstance().currentUser?.uid.toString()
                val uidRecipient = etRecipient.text.toString()
                val date = Date()
                val order = OrderModel(addressFrom,addressTo,price,dimensions,uidSender, uidRecipient,date)
                viewModel.sendOrder(order)
            }
        }
    }

    private fun removeMarker(marker: PlacemarkMapObject) {
        marker.let {
            mapObjectCollection.remove(it)
        }
    }


    //выделение домика
    private val tapListener = GeoObjectTapListener { geoObjectTapEvent ->
        val selectionMetadata: GeoObjectSelectionMetadata = geoObjectTapEvent
            .geoObject
            .metadataContainer
            .getItem(GeoObjectSelectionMetadata::class.java)
        viewBinding.mapview.mapWindow.map.selectGeoObject(selectionMetadata)
        false
    }


    //безумно важная вещь
    private val touchListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            viewModel.onMapTap(point){
                markerToAddress(it, viewBinding.etFrom)
            }
            //отправлять поинт на бек дял измерения расстояния

            if (startMarker != null) {
                removeMarker(startMarker!!)
            }
            startMarker =
                mapObjectCollection.addPlacemark(point, ImageProvider.fromBitmap(markerStart))

        }

        override fun onMapLongTap(map: Map, point: Point) {
            viewModel.onMapLongTap(point){
                markerToAddress(it, viewBinding.etTo)
            }
            if (finishMarker != null) {
                removeMarker(finishMarker!!)
            }
            finishMarker =
                mapObjectCollection.addPlacemark(point, ImageProvider.fromBitmap(markerFinish))
            //отправлять поинт на бек для измерения расстояния

        }
    }

    private fun markerToAddress(response: Response, editText: EditText) {
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
            editText.setText(addressText)
        } else {
            Toast.makeText(requireContext(), "Адрес не найден", Toast.LENGTH_SHORT).show()
        }

    }

    //самая полезная вещь, показывает адрес в тосте
    private val searchListenerEtFrom = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            markerToAddress(response, viewBinding.etFrom)
        }

        override fun onSearchError(p0: Error) {

        }
    }
    private val searchListenerEtTo = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            markerToAddress(response, viewBinding.etTo)
        }

        override fun onSearchError(p0: Error) {
        }
    }

    private fun requestLocationAccess() {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun requestLocationPermission() {
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

    private fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(requireContext(), art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
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

    private fun observerData() {
        with(viewModel) {
            setSearchCallbackListenerFrom {
                for (searchResult in it.collection.children) {
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
            setSearchCallbackListenerTo {
                for (searchResult in it.collection.children) {
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
            }
            lifecycleScope.launch {

                sendOrderFlow.collect { result ->
                    result?.let {
                        Toast.makeText(requireContext(), "заказ отправлен", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}