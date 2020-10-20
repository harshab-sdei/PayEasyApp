package com.peazyapp.peazy.controllers.ui.addcart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peazyapp.peazy.R
import com.peazyapp.peazy.controllers.ui.menu.MenuFragment
import com.peazyapp.peazy.models.addcart.Add_Item
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import kotlinx.android.synthetic.main.add_item_cart_list.view.*
import kotlinx.android.synthetic.main.add_item_cart_list.view.add_cart

class AddCartAdepter(var item: ArrayList<Add_Item>, val clickLister: (Add_Item) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.add_item_cart_list, parent, false)
        return MyViewHolder(
            listItem
        )
    }

    override fun getItemCount(): Int {
        Log.d("item", "item size=" + item.size)
        return item.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.view.bgimg.setBackgroundResource(R.drawable.lay_item_menu2)
        } else {
            holder.view.bgimg.setBackgroundResource(R.drawable.lay_item_menu)
        }

        holder.view.minus_cart.setOnClickListener {
            try {
                if (item.get(position).num_of_unit >= 1) {
                    holder.view.txtitem.text = "" + item.get(position).num_of_unit--
                    Constants.addcartlist.get(position).num_of_unit = item.get(position).num_of_unit
                    MenuFragment.itemCount--
                    MenuFragment.price_total -= item.get(position).price
                    clickLister(item.get(position))
                    notifyDataSetChanged()
                }

                if (item.get(position).num_of_unit == 0) {
                    removeItem(position)
                }
            } catch (e: Exception) {
            }
        }

        holder.view.add_cart.setOnClickListener {
            try {
                holder.view.txtitem.text = item.get(position).num_of_unit++.toString()
                clickLister(item.get(position))
                MenuFragment.itemCount++
                Constants.addcartlist.get(position).num_of_unit = item.get(position).num_of_unit
                MenuFragment.price_total += item.get(position).price
                notifyDataSetChanged()

            } catch (e: Exception) {
            }
        }

        holder.bind(item.get(position), clickLister)

    }

    fun removeItem(position: Int) {
        item.removeAt(position)
        notifyItemRemoved(position)
    }
}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Add_Item, clickLister: (Add_Item) -> Unit) {
        var index =
            UserPreferenc.getStringPreference(Constants.MENU_ITEM_PATH, "") + item.image

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
        view.txt_item_name.text = item.name
        view.txtitem.text = "" + item.num_of_unit
        view.txtprice.text =
            Constants.currency + String.format("%.2f", (item.price * item.num_of_unit))

    }


}