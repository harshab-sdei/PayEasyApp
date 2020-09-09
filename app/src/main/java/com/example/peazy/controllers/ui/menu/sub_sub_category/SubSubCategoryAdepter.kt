package com.example.peazy.controllers.ui.menu.sub_sub_category

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.peazy.R
import com.example.peazy.controllers.ui.menu.MenuFragment
import com.example.peazy.controllers.ui.menu.barmenuitemadepter.MenuItemAdepter
import com.example.peazy.models.addcart.Add_Item
import com.example.peazy.models.subsubcategory.SubItem
import com.example.peazy.models.subsubcategory.Subcategory
import com.example.peazy.utility.Constants
import kotlinx.android.synthetic.main.sub_sub_category_item.view.*
import org.json.JSONArray

class SubSubCategoryAdepter(
    var context: Context, var subCategory: List<Subcategory>,
    val clickLister: (Subcategory) -> Unit
) : RecyclerView.Adapter<MySubCategoryHolder>() {
    var row_index: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySubCategoryHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.sub_sub_category_item, parent, false)
        return MySubCategoryHolder(
            listItem
        )
    }

    override fun getItemCount(): Int {
        Log.d("MenuAdapter", "category size=" + subCategory.size)
        return subCategory.size
    }

    override fun onBindViewHolder(holder: MySubCategoryHolder, position: Int) {
        holder.view.item_title.setOnClickListener {
            clickLister(subCategory.get(position))
            row_index = position
            notifyDataSetChanged()
        }

        holder.bind(subCategory.get(position), clickLister)
        if (row_index == position) {
            with(holder) {
                view.item_title.setTextColor(view.context.getResources().getColor(R.color.orange))
            }
        } else {
            with(holder) {
                view.item_title.setTextColor(view.context.getResources().getColor(R.color.gray))
            }
        }
        val layoutManager = GridLayoutManager(context, 3)
        try {
            if (subCategory.get(position).subItem.size != null || subCategory.get(position).subItem.size > 0) {
                holder.view.recleview_menuitem!!.setLayoutManager(layoutManager)
                val listmenu = subCategory.get(position).subItem as ArrayList<SubItem>
                holder.view.recleview_menuitem.adapter =
                    MenuItemAdepter(
                        listmenu,
                        { selsectItem: SubItem -> barMenulistItemClick(selsectItem) })
            }
        } catch (e: Exception) {
        }
    }

    fun barMenulistItemClick(item: SubItem) {
        var isinsert = false

        for (additem in Constants.addcartlist) {

            try {
                if (additem.item_id == item.item_id && item.price > 0) {
                    additem.num_of_unit++
                    isinsert = true
                }

            } catch (e: java.lang.Exception) {
            }
        }
        if (!isinsert) {
            var img: String? = ""
            if (!item.image.isNullOrEmpty()) {
                img = item.image.get(0)
            }
            var addItem = Add_Item(
                img!!,
                item.item_id,
                item.name,
                item.price,
                item.num_of_unit
            )
            Constants.addcartlist.add(addItem)
        }


        print("add Item size" + Constants.addcartlist.size)
        MenuFragment.newInstance().updateOder()

    }

}

class MySubCategoryHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(subCategory: Subcategory, clickLister: (Subcategory) -> Unit) {
        val string = SpannableString(subCategory.name)
        string.setSpan(
            UnderlineSpan(),
            0,
            subCategory.name.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        view.item_title.text = string

    }


}