package com.peazyapp.peazy.controllers.ui.home.adepter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.peazyapp.peazy.R
import com.peazyapp.peazy.models.editprofile.InfoWindowData
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowGoogleMap(private val context: Context) : InfoWindowAdapter {
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = (context as Activity).layoutInflater
            .inflate(R.layout.custom_info_windows, null)
        val barname = view.findViewById<TextView>(R.id.barname)
        val address = view.findViewById<TextView>(R.id.baraddress)
        val ratingBar = view.findViewById<RatingBar>(R.id.rating)
        val infoWindowData = marker.tag as InfoWindowData?
        barname.setText(infoWindowData!!.barname)
        address.setText(infoWindowData!!.address)
        ratingBar.rating = infoWindowData!!.rating.toFloat()
        return view
    }
}