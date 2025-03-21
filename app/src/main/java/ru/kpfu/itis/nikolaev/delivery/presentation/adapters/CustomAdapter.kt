package ru.kpfu.itis.nikolaev.delivery.presentation.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.utils.ConvertDate.Companion.convertFullDateToSimple
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
        val iv_qr: ImageView = view.findViewById(R.id.iv_qr)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_order, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val order = orders[position]
        viewHolder.tv_status.text = order.status// Используем данные из OrderModel
        //viewHolder.tv_status.text = "в ожидании принятия"
        viewHolder.tv_date.text = convertFullDateToSimple(order.date)
        viewHolder.tv_info.text = order.addressTo
        viewHolder.iv_qr.setOnClickListener {  // Обрабатываем клик на элементе
            onClickListener.invoke(order)  // Вызываем onClickListener, передавая OrderModel
        }
    }


    override fun getItemCount(): Int = orders.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateOrders(newOrders: List<OrderModel>) {
        this.orders = newOrders
        notifyDataSetChanged()
    }
}