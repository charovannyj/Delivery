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
            // Define click listener for the ViewHolder's View
            tv_status = view.findViewById(R.id.tv_status)
            tv_data = view.findViewById(R.id.tv_data)
            tv_name = view.findViewById(R.id.tv_name)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_order, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tv_status.text = statusSet[position]
        viewHolder.tv_data.text = idSet[position]
        viewHolder.tv_name.text = nameSet[position]

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = statusSet.size
    fun updateOrders(statusSet: Array<String>, idSet: Array<String>, nameSet: Array<String>) {
        this.statusSet = statusSet
        this.idSet = idSet
        this.nameSet = nameSet
    }

}
