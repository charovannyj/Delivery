package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import ru.kpfu.itis.nikolaev.delivery.R

class ScannerFragment : Fragment(), ZBarScannerView.ResultHandler {
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
        try{
            var str = result.contents
            var arr = strToArray(str)
            val uidSender = arr[0]
            val uidRecipient = arr[1]
            val date = arr[2]
            Log.e("tag", uidSender.toString())
            Log.e("tag", uidRecipient.toString())
            Log.e("tag", date.toString())
            val dbInstance = FirebaseFirestore.getInstance()
            dbInstance.document("clients/${uidSender}/send/${date}")
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val currentStatus = documentSnapshot.getString("status")
                        val newStatus = when (currentStatus.toString()) {
                            getString(R.string.order_status_from_the_sender) -> getString(R.string.order_status_at_the_courier)
                            getString(R.string.order_status_at_the_courier) -> getString(R.string.order_status_from_the_recipient)
                            getString(R.string.order_status_from_the_recipient) -> getString(R.string.order_status_from_the_recipient)
                            else -> currentStatus.toString()
                        }

                        // Обновляем документы после получения newStatus
                        dbInstance.document("clients/${uidSender}/send/${date}")
                            .update("status", newStatus)
                            .addOnSuccessListener {
                                Log.d("TAG", "DocumentSnapshot updated successfully (sender)")
                            }
                            .addOnFailureListener { e ->
                                Log.w("TAG", "Error updating document (sender)", e)
                            }

                        dbInstance.document("clients/${uidRecipient}/get/${date}")
                            .update("status", newStatus)
                            .addOnSuccessListener {
                                Log.d("TAG", "DocumentSnapshot updated successfully (recipient)")
                            }
                            .addOnFailureListener { e ->
                                Log.w("TAG", "Error updating document (recipient)", e)
                            }

                        zbView.flash // Вызываем flash после обновления документов

                    } else {
                        Log.w("TAG", "Document does not exist")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error getting document", e)
                }
            zbView.flash
        } catch (e:Exception){
            onResume()
        }
    }

    private fun strToArray(string: String): List<String> {
        return listOf(
            string.split(", ")[0].substring(1),
            string.split(", ")[1],
            string.split(", ")[2].substring(0, string.split(", ")[2].length - 1)
        )
    }
}