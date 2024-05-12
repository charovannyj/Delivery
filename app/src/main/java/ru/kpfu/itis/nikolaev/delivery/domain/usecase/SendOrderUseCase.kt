package ru.kpfu.itis.nikolaev.delivery.domain.usecase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignUpModel

class SendOrderUseCase (private val dispatcher: CoroutineDispatcher,
) {
    val auth = FirebaseAuth.getInstance()
    val dbInstance =  FirebaseFirestore.getInstance()

    suspend operator fun invoke(orderModel: OrderModel): Boolean {
        return withContext(dispatcher) {
            try {
                sendOrder(orderModel)
                getOrder(orderModel)
                true // Успешная авторизация
            } catch (e: Exception) {
                false // Ошибка авторизации
            }
        }
    }
    private fun sendOrder(orderModel: OrderModel){
        val map: MutableMap<String, Any> = HashMap()
        map["addressFrom"] = orderModel.addressFrom
        map["addressTo"] = orderModel.addressTo
        map["price"] = orderModel.price
        map["dimensions"] = orderModel.dimensions
        map["uidRecipient"] = orderModel.uidRecipient
        map["uidSender"] = orderModel.uidSender
        map["date"] = orderModel.date

        dbInstance.document("clients/${orderModel.uidSender}/send/${orderModel.date}").set(map)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG",
                    "DocumentSnapshot added with ID: "
                )
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) } //заполнение данными

    }
    private fun getOrder(orderModel: OrderModel){
        val map: MutableMap<String, Any> = HashMap()
        map["addressFrom"] = orderModel.addressFrom
        map["addressTo"] = orderModel.addressTo
        map["price"] = orderModel.price
        map["dimensions"] = orderModel.dimensions
        map["uidRecipient"] = orderModel.uidRecipient
        map["uidSender"] = orderModel.uidSender
        map["date"] = orderModel.date

        dbInstance.document("clients/${orderModel.uidRecipient}/get/${orderModel.date}").set(map)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG",
                    "DocumentSnapshot added with ID: "
                )
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error adding document", e) } //заполнение данными

    }
}