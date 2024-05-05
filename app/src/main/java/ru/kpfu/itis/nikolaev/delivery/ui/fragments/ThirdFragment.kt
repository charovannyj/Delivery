package ru.kpfu.itis.nikolaev.delivery.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentThirdBinding


class ThirdFragment : Fragment() {

    private val viewBinding: FragmentThirdBinding by viewBinding(FragmentThirdBinding::bind)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["first"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815
        Log.e("TAG", FirebaseFirestore.getInstance().app.name)

// Add a new document with a generated ID

// Add a new document with a generated ID
        db.document("rrrrr/rerrret")
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG",
                    "DocumentSnapshot added with ID: "
                )
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }
        with(viewBinding){
            btnFastShipping.setOnClickListener{
                findNavController().navigate(R.id.fastShippingFragment)
            }
            btnEnterInApp.setOnClickListener{
                findNavController().navigate(R.id.navigation_mainFragment)
            }
        }
    }

}