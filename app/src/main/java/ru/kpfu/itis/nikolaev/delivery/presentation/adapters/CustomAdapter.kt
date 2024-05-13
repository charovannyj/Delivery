package ru.kpfu.itis.nikolaev.delivery.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.nikolaev.delivery.R

class CustomAdapter(
    private var statusSet: Array<String>,
    private var idSet: Array<String>,
    private var nameSet: Array<String>

) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_status: TextView
        val tv_data: TextView
        val tv_name: TextView

        init {
            tv_status = view.findViewById(R.id.tv_status)
            tv_data = view.findViewById(R.id.tv_data)
            tv_name = view.findViewById(R.id.tv_name)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_order, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tv_status.text = statusSet[position]
        viewHolder.tv_data.text = idSet[position]
        viewHolder.tv_name.text = nameSet[position]

    }

    override fun getItemCount() : Int = statusSet.size
    fun updateOrders(statusSet: Array<String>, idSet: Array<String>, nameSet: Array<String>) {
        this.statusSet = statusSet
        this.idSet = idSet
        this.nameSet = nameSet
    }
}