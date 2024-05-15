package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.repository.OrdersRepositoryImpl
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentThirdBinding


class ThirdFragment : Fragment() {
    private val viewBinding: FragmentThirdBinding by viewBinding(FragmentThirdBinding::bind)

    val dbInstance =  FirebaseFirestore.getInstance()
    val dbOffersGet = dbInstance.document("clients/${FirebaseAuth.getInstance().currentUser?.uid.toString()}/offers/get")
    val dbOffersSend = dbInstance.document("clients/${FirebaseAuth.getInstance().currentUser?.uid.toString()}/offers/send")
    val dbProfile = dbInstance.document("clients/${FirebaseAuth.getInstance().currentUser?.uid.toString()}")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_third, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("TAG", FirebaseFirestore.getInstance().app.name)
        //fetchData()
        //¯\_(ツ)_/¯
// Add a new document with a generated ID

// Add a new document with a generated ID


        //saveData()


        with(viewBinding){
            btnFastShipping.setOnClickListener{
                findNavController().navigate(R.id.fastShippingFragment)
            }
            btnEnterInApp.setOnClickListener{
                findNavController().navigate(R.id.navigation_mainFragment)
            }
        }

    }
    /*private fun saveUser(){
        val user: MutableMap<String, Any> = HashMap()
        user["first"] = "wwowoowwoowow"
        db.set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG",
                    "DocumentSnapshot added with ID: "
                )
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) } //заполнение данными

    }
    private fun fetchData(){
        db.get()
            .addOnSuccessListener { task ->
                if (task.exists()) {
                    var str1 = task.get("first")
                    Log.e("TAG", str1 as String)
                } else {
                    Log.e("TAG", "Error getting documents.")
                }
            }
    }*/

}