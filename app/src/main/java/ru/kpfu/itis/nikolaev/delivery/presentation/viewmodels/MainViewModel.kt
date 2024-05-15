package ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.data.repository.OrdersRepositoryImpl
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignInModel
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.GetOrderUseCase
import ru.kpfu.itis.nikolaev.delivery.domain.usecase.SignInUseCase

class MainViewModel : ViewModel() {

    private var _ordersGetFlow = MutableSharedFlow<List<OrderModel>>(1)
    val ordersGetFlow: SharedFlow<List<OrderModel>?>
        get() = _ordersGetFlow

    private var _ordersSendFlow = MutableSharedFlow<List<OrderModel>>(1)
    val ordersSendFlow: SharedFlow<List<OrderModel>?>
        get() = _ordersSendFlow



    fun getOrders() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        viewModelScope.launch {
            try {
                val resultGets =  GetOrderUseCase(Dispatchers.IO).invoke(uid, "get")
                val resultSends = GetOrderUseCase(Dispatchers.IO).invoke(uid, "send")
                _ordersGetFlow.emit(resultGets)
                _ordersSendFlow.emit(resultSends)
            } catch (e: Exception) {
                Log.e("TAG_error", "error")
            }
        }
    }
}