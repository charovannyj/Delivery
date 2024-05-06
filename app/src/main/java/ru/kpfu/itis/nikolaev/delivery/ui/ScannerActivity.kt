package ru.kpfu.itis.nikolaev.delivery.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import ru.kpfu.itis.nikolaev.delivery.R

class ScannerActivity : Fragment(), ZBarScannerView.ResultHandler {
    private lateinit var zbView: ZBarScannerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        zbView = ZBarScannerView(requireContext())
        return zbView.rootView
    }

    override fun onResume() {
        super.onResume()
        zbView.setResultHandler(this)
        zbView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        zbView.stopCamera()
    }

    override fun handleResult(result: Result) {
        Log.d("MyLog",result.contents.toString())
    }
}