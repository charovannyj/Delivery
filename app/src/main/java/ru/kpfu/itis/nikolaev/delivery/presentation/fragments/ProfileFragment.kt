package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentProfileBinding
import ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels.ProfileViewModel


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewBinding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var ivProfile: ImageView
    private lateinit var btnChangePhoto: Button
    var uidd : String? = null
    var name : String? = null

    var surname : String? = null

    var email : String? = null
    var user =  FirebaseAuth.getInstance().currentUser

    init {
        uidd = user?.uid



    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivProfile = view.findViewById(R.id.iv_profile)


        with(viewBinding){
            tvUid.text = uidd
            val sharedPreferences =
                requireActivity().getSharedPreferences(
                    "sharedPrefs",
                    Context.MODE_PRIVATE
                )

            etName.setText(sharedPreferences.getString("name", null))
            etSurname.setText(sharedPreferences.getString("secondName", null))
            etEmail.setText(sharedPreferences.getString("email", null))

        }
    }

    /*private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)

            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val exif = ExifInterface(inputStream!!)

            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            val rotationAngle = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            val matrix = Matrix()
            matrix.postRotate(rotationAngle.toFloat())

            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            ivProfile.setImageBitmap(rotatedBitmap)
            inputStream.close()
        }
    }*/
    private fun observerData() {
        viewModel.getUserData(user?.uid.toString())
        with(viewModel) {
            lifecycleScope.launch {
                currentUserDataFlow.collect {
                    with(viewBinding) {
                        val sharedPreferences =
                            requireActivity().getSharedPreferences(
                                "sharedPrefs",
                                Context.MODE_PRIVATE
                            )

                        etName.setText(sharedPreferences.getString("name", null))
                        etSurname.setText(sharedPreferences.getString("secondName", null))
                        etEmail.setText(sharedPreferences.getString("email", null))
                    }
                }
            }
        }
    }
}