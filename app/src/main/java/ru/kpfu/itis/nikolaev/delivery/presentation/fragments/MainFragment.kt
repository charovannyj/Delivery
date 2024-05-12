package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.annotation.SuppressLint
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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.Keys
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.dao.Dao
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.data.repository.RoomOrdersRepository
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
            rbGet.setOnClickListener {
                customAdapter?.updateOrders(
                    ordersGet!!.map { it.addressTo }.toTypedArray(), // addressTo
                    ordersGet!!.map { it.date.toString() }.toTypedArray(), // dateSet - конвертируйте даты в строки
                    ordersGet!!.map {  "status"  }.toTypedArray(), // statusSet - замените "status" на фактический статус
                )

                customAdapter?.notifyDataSetChanged()
            }
            rbSend.setOnClickListener{
                customAdapter?.updateOrders(
                    ordersSend!!.map { it.addressTo }.toTypedArray(), // addressTo
                    ordersSend!!.map { it.date.toString() }.toTypedArray(), // dateSet - конвертируйте даты в строки
                    ordersSend!!.map {  "status"  }.toTypedArray(), // statusSet - замените "status" на фактический статус
                )

                customAdapter?.notifyDataSetChanged()
            }
            val statusSet = arrayOf("null")
            val dateSet = arrayOf("null")
            var addressTo1 = arrayOf("null")
            customAdapter = CustomAdapter(statusSet,dateSet,addressTo1)
            val recyclerView: RecyclerView = recyclerView
            recyclerView.adapter = customAdapter


        }
    }
    init {
        viewModel.getOrders()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observerData() {
        with(viewModel) {
            lifecycleScope.launch {
                //поменять на другой флоу
                ordersGetFlow.collect { orders ->
                    orders?.let {
                        /*for (order in orders){
                            RoomOrdersRepository.insertData(order)
                        }*/
                        ordersGet = orders
                    }
                }

            }
            lifecycleScope.launch {
                ordersSendFlow.collect { orders ->
                    orders?.let {
                        /*for (order in orders){
                            RoomOrdersRepository.insertData(order)
                        }
                        ordersGet = RoomOrdersRepository.fetchData()*/
                        ordersSend = orders

                    }
                }

            }
        }

    }
}
