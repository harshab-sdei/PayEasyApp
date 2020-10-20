package com.peazyapp.peazy.controllers.ui.orderhistory.adepter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peazyapp.peazy.R
import com.peazyapp.peazy.controllers.ui.confirmorderstatus.adepter.OrderConfirmItemAdepter
import com.peazyapp.peazy.models.oderhistory.User
import com.peazyapp.peazy.models.oderhistory.Item
import com.peazyapp.peazy.utility.Constants
import kotlinx.android.synthetic.main.order_history_item.view.*

class OrderHistoryAdepter(var item: List<User>, val clickLister: (User) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    lateinit var viewAdapter: OderHistoryItemAdepter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.order_history_item, parent, false)
        return MyViewHolder(
            listItem
        )
    }

    override fun getItemCount(): Int {
        Log.d("item", "item size=" + item.size)
        return item.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(item.get(position), clickLister)
        holder.view.recleview_history.layoutManager =
            LinearLayoutManager(holder.view.context, LinearLayoutManager.VERTICAL, false)

        viewAdapter =
            OderHistoryItemAdepter(
                item.get(position).item,
                { selsectItem: Item -> listItemClick(selsectItem) })
        holder.view.recleview_history.adapter = viewAdapter
    }

    private fun listItemClick(selsectItem: Item) {

    }


}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: User, clickLister: (User) -> Unit) {
        view.barname.text = "" + item.bar_name
        view.subtotal.text = "" + Constants.currency + item.amount

    }


}