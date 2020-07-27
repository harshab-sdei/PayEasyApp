package com.example.peazy.controllers.ui.menu.subcategory_adepter

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.peazy.R
import com.example.peazy.models.subcategory.SubcategoryX
import kotlinx.android.synthetic.main.sub_catagory_item.view.*


class SubCategoryAdepter(
    var subCategory: List<SubcategoryX>,
    val clickLister: (SubcategoryX) -> Unit
) : RecyclerView.Adapter<MyViewHolder>() {
    var row_index: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.sub_catagory_item, parent, false)
        return MyViewHolder(
            listItem
        )
    }

    override fun getItemCount(): Int {
        Log.d("MenuAdapter", "category size=" + subCategory.size)
        return subCategory.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.bt_subcat.setOnClickListener {
            clickLister(subCategory.get(position))
            row_index = position
            notifyDataSetChanged()
        }

        holder.bind(subCategory.get(position), clickLister)
        if (row_index == position) {
            with(holder) {
                view.txtsub_cat.setTextColor(view.context.getResources().getColor(R.color.orange))
            }
        } else {
            with(holder) {
                view.txtsub_cat.setTextColor(view.context.getResources().getColor(R.color.gray))
            }
        }
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(subCategory: SubcategoryX, clickLister: (SubcategoryX) -> Unit) {
        val string = SpannableString(subCategory.name)
        string.setSpan(
            UnderlineSpan(),
            0,
            subCategory.name.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        view.txtsub_cat.text = string

    }


}