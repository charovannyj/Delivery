package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.Keys
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentMainBinding
import ru.kpfu.itis.nikolaev.delivery.presentation.adapters.CustomAdapter
import ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels.AuthViewModel
import ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels.MainViewModel


class MainFragment : Fragment() {

    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    private var ordersGet: List<OrderModel>? = null
    private var ordersSend: List<OrderModel>? = null
    private var dataGetting = false
    private var customAdapter: CustomAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Keys.authorized = true
        observerData()
        with(viewBinding){
            viewModel.getOrders()
            val statusSet = arrayOf("null")
            val dateSet = arrayOf("null")
            var addressTo1 = arrayOf("null")
            //Thread.sleep(10000)
            customAdapter = CustomAdapter(statusSet,dateSet,addressTo1)
            val recyclerView: RecyclerView = recyclerView
            recyclerView.adapter = customAdapter


        }
    }
    private fun observerData() {
        with(viewModel) {
            lifecycleScope.launch {
                //поменять на другой флоу
                ordersGetFlow.collect { orders ->
                    orders?.let {
                        ordersGet = orders
                        customAdapter?.updateOrders(
                            orders.map { it.addressTo }.toTypedArray(), // addressTo
                            orders.map { it.date.toString() }.toTypedArray(), // dateSet - конвертируйте даты в строки
                            orders.map {  "status"  }.toTypedArray(), // statusSet - замените "status" на фактический статус
                        )

                        // Уведомляем адаптер об изменении данных
                        customAdapter?.notifyDataSetChanged()
                        Log.e("Tag", ordersGet.toString())
                    }
                }

            }
            /*lifecycleScope.launch {
                ordersSendFlow.collect { orders ->
                    orders?.let {
                        ordersSend = orders

                    }
                }

            }*/
        }

    }
}
