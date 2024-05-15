package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
class QRDialogFragment(private val order: OrderModel) : DialogFragment() {

    private val viewBinding: FragmentQrDialogBinding by viewBinding(FragmentQrDialogBinding::bind)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Инициализируем Dialog
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(viewBinding.root) // Используем viewBinding.root

        // Генерация QR-кода
        val qrCodeData = order.date.timeInMillis.toString()
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(qrCodeData, BarcodeFormat.QR_CODE, 750, 750)
        viewBinding.imageView.setImageBitmap(bitmap) // Используем viewBinding.imageView

        return dialog
    }
}