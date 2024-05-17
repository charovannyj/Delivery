package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.checker.index.qual.LengthOf
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignUpModel
import ru.kpfu.itis.nikolaev.delivery.utils.ConvertDate

class SendOrderUseCase (private val dispatcher: CoroutineDispatcher,
) {
    val auth = FirebaseAuth.getInstance()
    val dbInstance =  FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(orderModel: OrderModel): Boolean {
        return withContext(dispatcher) {
            try {
                sendOrder(orderModel)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendOrder(orderModel: OrderModel){
        val normalDate = ConvertDate.convertFullDateToSimple(orderModel.date)
        val map: MutableMap<String, Any> = HashMap()
        map["addressFrom"] = orderModel.addressFrom
        map["addressTo"] = orderModel.addressTo
        map["price"] = orderModel.price
        map["dimensions"] = orderModel.dimensions
        map["uidRecipient"] = orderModel.uidRecipient
        map["uidSender"] = orderModel.uidSender
        map["date"] = normalDate
        map["status"] = orderModel.status

        dbInstance.document("clients/${orderModel.uidSender}/send/${normalDate}").set(map)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG",
                    "DocumentSnapshot added with ID: "
                )
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }
        dbInstance.document("clients/${orderModel.uidRecipient}/get/${normalDate}").set(map)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG",
                    "DocumentSnapshot added with ID: "
                )
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) }
    }

}