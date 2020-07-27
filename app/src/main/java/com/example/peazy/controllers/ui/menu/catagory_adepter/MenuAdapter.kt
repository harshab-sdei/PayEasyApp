package com.example.peazy.controllers.ui.menu.catagory_adepter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.peazy.R
import com.example.peazy.models.category.Category
import com.example.peazy.utility.Constants
import com.example.peazy.utility.appconfig.UserPreferenc
import kotlinx.android.synthetic.main.category_list.view.*

class MenuAdapter(var category: List<Category>, val clickLister: (Category) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    var row_index: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.category_list, parent, false)
        return MyViewHolder(
            listItem
        )
    }

    override fun getItemCount(): Int {
        Log.d("MenuAdapter", "category size=" + category.size)
        return category.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.bt_cat.setOnClickListener {
            clickLister(category.get(position))
            row_index = position
            notifyDataSetChanged()
        }

        holder.bind(category.get(position), clickLister)
        if (row_index == position) {
            holder.view.bt_cat.setBackgroundResource(R.drawable.button_dark_orange)
        } else {
            holder.view.bt_cat.setBackgroundResource(R.drawable.button_light_sky)
        }
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(category: Category, clickLister: (Category) -> Unit) {
        var index =
            UserPreferenc.getStringPreference(Constants.MENU_CAT_IMG_PATH, "") + category.image

        try {
            if (index !== null) {
                Glide.with(this.view.context)
                    .load(index)
                    .error(R.drawable.logo)
                    .into(view.cat_img)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        view.cat_name.text = category.name
        /*  view.bt_cat.setOnClickListener{
              clickLister(category)
              row_index=true
          }*/

    }


}