package ru.kpfu.itis.nikolaev.delivery.ui.fragments

import android.app.Activity
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentSupportBinding
import ru.kpfu.itis.nikolaev.delivery.ui.ScannerActivity

class SupportFragment : Fragment(R.layout.fragment_support) {
    private val viewBinding: FragmentSupportBinding by viewBinding(FragmentSupportBinding::bind)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding){
            val bScanner = btn
            bScanner.setOnClickListener {
                checkCameraPermission()
            }
        }
    }

    private fun checkCameraPermission(){
        if(ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA),12)
        }
        else{
            startActivity(Intent(requireActivity(), ScannerActivity::class.java))
        }
    }
    companion object {
        private const val REQUEST_CODE = 100
    }


}