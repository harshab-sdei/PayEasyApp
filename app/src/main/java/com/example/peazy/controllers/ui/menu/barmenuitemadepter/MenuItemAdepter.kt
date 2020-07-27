package com.example.peazy.controllers.ui.menu.barmenuitemadepter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.peazy.R
import com.example.peazy.controllers.ui.menu.MenuFragment
import com.example.peazy.models.menu_item.Item
import com.example.peazy.utility.Constants
import com.example.peazy.utility.appconfig.UserPreferenc
import kotlinx.android.synthetic.main.bar_menu_item.view.*

class MenuItemAdepter(var item: List<Item>, val clickLister: (Item) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    var row_index: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.bar_menu_item, parent, false)
        return MyViewHolder(
            listItem
        )
    }

    override fun getItemCount(): Int {
        Log.d("item", "item size=" + item.size)
        return item.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.additem.setOnClickListener {
            clickLister(item.get(position))
            row_index = position
            notifyDataSetChanged()
        }

        holder.bind(item.get(position), clickLister)
        /* if (row_index==position) {
             holder.view.bt_cat.setBackgroundResource(R.drawable.button_dark_orange)
         } else {
             holder.view.bt_cat.setBackgroundResource(R.drawable.button_light_sky)
         }*/
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Item, clickLister: (Item) -> Unit) {
        var index =
            UserPreferenc.getStringPreference(Constants.MENU_ITEM_PATH, "") + item.image.get(0)

        try {
            if (index !== null) {
                Glide.with(this.view.context)
                    .load(index)
                    .error(R.drawable.logo)
                    .into(view.item_img)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        view.itemname.text = item.name
        view.ite_desc.text = item.description
        view.item_price.text = "$" + item.price
    }


}