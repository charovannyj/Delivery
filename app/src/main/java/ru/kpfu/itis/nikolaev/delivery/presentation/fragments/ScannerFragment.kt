package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
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
        Log.d("MyLog",result.contents)
        var str = result.contents
        Log.d("MyLog", strToArray(str).contentToString())
        var arr: String? = null
        try{
            arr = strToArray(str).contentToString()
            return
        }
        catch (e:Exception){
            handleResult(result)
        }
        val uid = strToArray(str)[0]
        val date = strToArray(str)[1]
        Log.e("tag", uid)
        Log.e("tag", date)

        val dbInstance =  FirebaseFirestore.getInstance()

        dbInstance.document("clients/${uid}/get/${date}")
            .update("status", "у курьера")
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot updated successfully")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error updating document", e)
            }
        zbView.flash
    }
    private fun strToArray(string : String) : Array<String>{
        return arrayOf(string.split(", ")[0].substring(1), string.split(", ")[1].substring(0, string.split(", ")[1].length - 1))
    }
}