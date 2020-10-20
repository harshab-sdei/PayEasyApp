package com.peazyapp.peazy.controllers.ui.bardetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.peazyapp.peazy.R

class BarDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bar_detail_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, BarDetailFragment.newInstance())
                .commitNow()
        }
    }
}