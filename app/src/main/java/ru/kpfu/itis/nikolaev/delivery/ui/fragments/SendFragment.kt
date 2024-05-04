package ru.kpfu.itis.nikolaev.delivery.ui.fragments


import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
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
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentSendBinding
import java.lang.StringBuilder


class SendFragment : Fragment(R.layout.fragment_send) {
    private val viewBinding: FragmentSendBinding by viewBinding(FragmentSendBinding::bind)
    private val startLocation = Point(55.792139, 49.122135)
    lateinit var searchManager: SearchManager
    lateinit var searchSession: Session
    lateinit var marker : Bitmap
    private val zoomValue = 15.0f
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) requestLocationAccess()
        }
    private lateinit var mapView: MapView
    private lateinit var mapObjectCollection: MapObjectCollection
    private lateinit var placemarkMapObject: PlacemarkMapObject

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
        with(viewBinding) {
            mapView = mapview
            marker = createBitmapFromVector(R.drawable.location_map_pin_mark_icon_148684)!!

            mapObjectCollection = mapView.mapWindow.map.mapObjects
            placemarkMapObject = mapObjectCollection.addPlacemark(startLocation, ImageProvider.fromBitmap(marker))
            placemarkMapObject.addTapListener(mapObjectTapListener) //бесполезная штука, показывает корды метки заранее написанной
            mapView.mapWindow.map.addTapListener(tapListener)     //выделение домика
            mapView.mapWindow.map.move(
                CameraPosition(startLocation,zoomValue,0.0f,0.0f),
                Animation(Animation.Type.SMOOTH, 2f), null) //мув к заранее выбранному месту
            val mapKit : MapKit = MapKitFactory.getInstance()
            requestLocationPermission()
            val locationMapkit = mapKit.createUserLocationLayer(mapview.mapWindow)
            locationMapkit.isVisible = true
            searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE) //важная вещь
            mapview.mapWindow.map.addInputListener(inputListener) // Добавляем слушатель тапов по карте с извлечением информации


        }
    }

    private fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(requireContext(), art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    //выделение домика
    private val tapListener = object : GeoObjectTapListener {
        override fun onObjectTap(geoObjectTapEvent: GeoObjectTapEvent): Boolean {
            val selectionMetadata: GeoObjectSelectionMetadata = geoObjectTapEvent
                .geoObject
                .metadataContainer
                .getItem(GeoObjectSelectionMetadata::class.java)
            viewBinding.mapview.mapWindow.map.selectGeoObject(selectionMetadata)
            return false
        }
    }


    //...
    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            searchSession = searchManager.submit(point, 20, SearchOptions(), searchListener)
        }
        override fun onMapLongTap(map: Map, point: Point) {}
    }


    //самая полезная вещь, показывает адрес в тосте
    private val searchListener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            val street = response.collection.children.firstOrNull()?.obj
                ?.metadataContainer
                ?.getItem(ToponymObjectMetadata::class.java)
                ?.address
                ?.components
                ?.firstOrNull { it.kinds.contains(Address.Component.Kind.STREET)}
                ?.name ?: "Информация об улице не найдена"

            Toast.makeText(requireContext(), street, Toast.LENGTH_SHORT).show()
        }

        override fun onSearchError(p0: Error) {
            TODO("Not yet implemented")
        }

    }

    fun requestLocationAccess () {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
    private fun requestLocationPermission(){
        if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 0)
            return
            }
    }

    //бесполезная штука, показывает корды метки заранее написанной
    private val mapObjectTapListener = object : MapObjectTapListener{
        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean{
            Toast.makeText(requireContext(), point.latitude.toString(), Toast.LENGTH_SHORT).show()
            return true
        }
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
}