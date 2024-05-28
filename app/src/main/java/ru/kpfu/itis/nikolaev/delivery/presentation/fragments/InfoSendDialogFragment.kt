package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.app.Dialog
import android.graphics.Color
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
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.databinding.InfoSendDialogBinding
import ru.kpfu.itis.nikolaev.delivery.databinding.QrDialogBinding
import ru.kpfu.itis.nikolaev.delivery.utils.ConvertDate
class InfoSendDialogFragment(

) : DialogFragment() {
    private var _binding: InfoSendDialogBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = InfoSendDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.window?.attributes?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.dimAmount = 0.8f
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)


        dialog.setContentView(binding.root)

        return dialog
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
