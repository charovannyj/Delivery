package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentMainBinding
import ru.kpfu.itis.nikolaev.delivery.presentation.adapters.CustomAdapter
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
        observerData()
        with(viewBinding) {
            viewModel.getOrders()

            // Инициализируем адаптер с пустыми данными
            customAdapter = CustomAdapter(arrayOf(), arrayOf(), arrayOf())
            val recyclerView: RecyclerView = recyclerView
            recyclerView.adapter = customAdapter
            recyclerView.visibility = View.GONE // Изначально скрываем recyclerView
            rbGet.setOnClickListener {
                recyclerView.visibility = View.VISIBLE
                customAdapter?.updateOrders(
                    ordersGet!!.map { it.addressTo }.toTypedArray(), // addressTo
                    ordersGet!!.map { it.date.toString() }.toTypedArray(), // dateSet - конвертируйте даты в строки
                    ordersGet!!.map {  "status"  }.toTypedArray(), // statusSet - замените "status" на фактический статус
                )
                customAdapter?.notifyDataSetChanged()
            }

            rbSend.setOnClickListener {
                recyclerView.visibility = View.VISIBLE
                customAdapter?.updateOrders(
                    ordersSend!!.map { it.addressTo }.toTypedArray(), // addressTo
                    ordersSend!!.map { it.date.toString() }.toTypedArray(), // dateSet - конвертируйте даты в строки
                    ordersSend!!.map {  "status"  }.toTypedArray(), // statusSet - замените "status" на фактический статус

                )
                customAdapter?.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observerData() {
        with(viewModel) {
            lifecycleScope.launch {
                ordersGetFlow.collect { orders ->
                    ordersGet = orders.orEmpty() // Используйте orEmpty() для пустого списка
                    // Обновляем адаптер при получении новых данных
                    if (viewBinding.rbGet.isChecked) { // Обновляем, только если rbGet выбран
                        customAdapter?.updateOrders(
                            ordersGet!!.map { it.addressTo }.toTypedArray(), // addressTo
                            ordersGet!!.map { it.date.toString() }
                                .toTypedArray(), // dateSet - конвертируйте даты в строки
                            ordersGet!!.map { "status" }
                                .toTypedArray(), // statusSet - замените "status" на фактический статус
                        )
                        customAdapter?.notifyDataSetChanged()
                    }
                }
            }
            lifecycleScope.launch {
                ordersSendFlow.collect { orders ->
                    ordersSend = orders.orEmpty() // Используйте orEmpty() для пустого списка
                    // Обновляем адаптер при получении новых данных
                    if (viewBinding.rbSend.isChecked) { // Обновляем, только если rbSend выбран
                        customAdapter?.updateOrders(
                            ordersSend!!.map { it.addressTo }.toTypedArray(), // addressTo
                            ordersSend!!.map { it.date.toString() }
                                .toTypedArray(), // dateSet - конвертируйте даты в строки
                            ordersSend!!.map { "status" }
                                .toTypedArray(), // statusSet - замените "status" на фактический статус
                        )
                        customAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}
