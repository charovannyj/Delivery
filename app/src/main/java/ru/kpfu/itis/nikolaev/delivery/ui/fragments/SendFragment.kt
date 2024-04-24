package ru.kpfu.itis.nikolaev.delivery.ui.fragments

import android.content.pm.PackageManager
import android.graphics.Camera
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.yandex.mapkit.map.Map
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentSendBinding


class SendFragment : Fragment(R.layout.fragment_send) {
    private val viewBinding: FragmentSendBinding by viewBinding(FragmentSendBinding::bind)
    private lateinit var mapView: MapView

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
            mapView.mapWindow.map.move(
                CameraPosition(Point(55.792139, 49.122135),11.0f,0.0f,0.0f),
                Animation(Animation.Type.SMOOTH, 5f), null)
            val mapKit : MapKit = MapKitFactory.getInstance()
            requestLocationPermission()
            val locationMapkit = mapKit.createUserLocationLayer(mapview.mapWindow)
            locationMapkit.isVisible = true
            
            /*val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.pin_placeholder)
            val placemark = mapView.map.mapObjects.addPlacemark().apply {
                geometry = Point(59.935493, 30.327392)
                setIcon(imageProvider)
            }*/


        }
    }
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) requestLocationAccess()
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


    private val placemarkTapListener = MapObjectTapListener { _, point ->
        Toast.makeText(
            requireActivity(),
            "Tapped the point (${point.longitude}, ${point.latitude})",
            Toast.LENGTH_SHORT
        ).show()
        true
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