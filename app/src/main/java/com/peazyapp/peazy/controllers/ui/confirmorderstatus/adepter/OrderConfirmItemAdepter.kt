package com.peazyapp.peazy.controllers.ui.confirmorderstatus.adepter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peazyapp.peazy.R
import com.peazyapp.peazy.models.verifypay.Item
import com.peazyapp.peazy.utility.Constants
import kotlinx.android.synthetic.main.orderconfirm_item.view.*

class OrderConfirmItemAdepter(var item: List<Item>, val clickLister: (Item) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.orderconfirm_item, parent, false)
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
    }


}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Item, clickLister: (Item) -> Unit) {
        var num_of_unit: Int = item.quantity.trim().toInt()
        view.txt_item_name.text = item.item_name
        view.txtitem.text = "" + item.quantity
        view.txtprice.text =
            Constants.currency + (item.price * num_of_unit)

    }


}