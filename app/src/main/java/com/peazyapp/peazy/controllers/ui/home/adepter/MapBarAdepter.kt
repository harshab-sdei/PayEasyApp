package com.peazyapp.peazy.controllers.ui.home.adepter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peazyapp.peazy.R
import com.peazyapp.peazy.models.nearby.Bar
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import kotlinx.android.synthetic.main.nearby_map_bar_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class MapBarAdepter(var item: List<Bar>, val clickLister: (Bar) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    var row_index: Int = 0
    lateinit var FilterList: List<Bar>

    init {
        FilterList = item

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.nearby_map_bar_item, parent, false)
        return MyViewHolder(
            listItem
        )
    }

    override fun getItemCount(): Int {
        Log.d("item", "item size=" + item.size)
        return FilterList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.lay_bar.setOnClickListener {
            clickLister(FilterList.get(position))
            row_index = position
            // notifyDataSetChanged()
        }
        holder.view.barimg.setOnClickListener {
            clickLister(FilterList.get(position))
        }

        holder.bind(FilterList[position], clickLister)
        /* if (row_index==position) {
             holder.view.bt_cat.setBackgroundResource(R.drawable.button_dark_orange)
         } else {
             holder.view.bt_cat.setBackgroundResource(R.drawable.button_light_sky)
         }*/

    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    FilterList = item
                } else {
                    val resultList = ArrayList<Bar>()
                    for (row in item) {
                        if (row.name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    FilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = FilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                FilterList = results?.values as List<Bar>
                notifyDataSetChanged()
            }

        }
    }


}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Bar, clickLister: (Bar) -> Unit) {

        var index =
            UserPreferenc.getStringPreference(Constants.BAR_DETAIL_IMG_PATH, "") + item.image.get(0)

        try {
            if (index !== null) {
                Glide.with(this.view.context)
                    .load(index)
                    .error(R.drawable.logo)
                    .into(view.barimg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        view.barname.text = item.name
        view.distance.text = "" + item.distance / 1000 + "Km"
        view.rating.rating = 4.5F
    }


}