package ru.kpfu.itis.nikolaev.delivery.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomAdapter(
    private val onClickListener: (OrderModel) -> Unit,
    private var orders: List<OrderModel> = emptyList()
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_status: TextView = view.findViewById(R.id.tv_status)
        val tv_date: TextView = view.findViewById(R.id.tv_date)
        val tv_info: TextView = view.findViewById(R.id.tv_info)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_order, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val order = orders[position]
        //viewHolder.tv_status.text = order.status// Используем данные из OrderModel
        viewHolder.tv_status.text = "в ожидании принятия"
        viewHolder.tv_date.text = convertFullDateToSimple(order.date)
        viewHolder.tv_info.text = order.addressTo
        viewHolder.itemView.setOnClickListener {  // Обрабатываем клик на элементе
            onClickListener.invoke(order)  // Вызываем onClickListener, передавая OrderModel
        }
    }
    private fun convertFullDateToSimple(date : Calendar) : String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(date.time)
    }

    override fun getItemCount(): Int = orders.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateOrders(newOrders: List<OrderModel>) {
        this.orders = newOrders
        notifyDataSetChanged()
    }
}