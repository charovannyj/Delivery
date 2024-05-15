package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentQrBinding

class QrFragment : Fragment(R.layout.fragment_qr) {

    private val viewBinding: FragmentQrBinding by viewBinding(FragmentQrBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding){
            val image = imageView
            val edittext = et
            val bGenerate = btn
            bGenerate.setOnClickListener {
                try {
                    val barcodeEncode: BarcodeEncoder = BarcodeEncoder()
                    val bitmap : Bitmap = barcodeEncode.encodeBitmap(edittext.getText().toString(), BarcodeFormat.QR_CODE, 750, 750)
                    image.setImageBitmap(bitmap)
                } catch (_: WriterException){

                }
            }
        }
    }

}