package ru.kpfu.itis.nikolaev.delivery.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.nikolaev.delivery.R

class CustomAdapter(
    private var statusSet: Array<String>,
    private var dateSet: Array<String>,
    private var infoSet: Array<String>

) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_status: TextView
        val tv_date: TextView
        val tv_info: TextView

        init {
            tv_status = view.findViewById(R.id.tv_status)
            tv_date = view.findViewById(R.id.tv_date)
            tv_info = view.findViewById(R.id.tv_info)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_order, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tv_status.text = statusSet[position]
        viewHolder.tv_date.text = dateSet[position]
        viewHolder.tv_info.text = infoSet[position]

    }

    override fun getItemCount() : Int = statusSet.size
    fun updateOrders(statusSet: Array<String>, dateSet: Array<String>, infoSet: Array<String>) {
        this.statusSet = statusSet
        this.dateSet = dateSet
        this.infoSet = infoSet
    }
}