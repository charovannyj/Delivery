package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.databinding.QrDialogBinding
import ru.kpfu.itis.nikolaev.delivery.utils.ConvertDate

class QRDialogFragment(
    private val user : FirebaseUser,
    private val order: OrderModel
) : DialogFragment() {
    private var _binding: QrDialogBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = QrDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.window?.attributes?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.dimAmount = 0.8f
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)


        dialog.setContentView(binding.root)

        // Генерация QR-кода
        val array = arrayOf(order.uidSender, order.uidRecipient, ConvertDate.convertFullDateToSimple(order.date))
        val qrCodeData = array
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(qrCodeData.contentToString(), BarcodeFormat.QR_CODE, 750, 750)
        binding.imageView.setImageBitmap(bitmap)

        return dialog
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}