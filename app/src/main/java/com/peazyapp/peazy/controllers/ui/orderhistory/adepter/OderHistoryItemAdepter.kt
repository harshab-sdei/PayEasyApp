package com.peazyapp.peazy.controllers.ui.orderhistory.adepter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peazyapp.peazy.R
import com.peazyapp.peazy.models.oderhistory.Item
import com.peazyapp.peazy.utility.Constants
import kotlinx.android.synthetic.main.orderconfirm_item.view.*

class OderHistoryItemAdepter(var item: List<Item>, val clickLister: (Item) -> Unit) :
    RecyclerView.Adapter<MyViewHolders>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolders {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.orderconfirm_item, parent, false)
        return MyViewHolders(
            listItem
        )
    }

    override fun getItemCount(): Int {
        Log.d("item", "item size=" + item.size)
        return item.size
    }

    override fun onBindViewHolder(holder: MyViewHolders, position: Int) {
        holder.bind(item.get(position), clickLister)
    }


}

class MyViewHolders(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Item, clickLister: (Item) -> Unit) {
        var num_of_unit: Int = item.quantity.trim().toInt()
        view.txt_item_name.text = item.item_name
        view.txtitem.text = "" + item.quantity
        view.txtprice.text =
            Constants.currency + (item.price * num_of_unit)

    }


}