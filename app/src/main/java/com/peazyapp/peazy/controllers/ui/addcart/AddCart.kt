package com.peazyapp.peazy.controllers.ui.addcart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.peazyapp.peazy.R
import com.peazyapp.peazy.controllers.ui.addcart.ui.main.AddCartFragment

class AddCart : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_cart_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddCartFragment.newInstance())
                .commitNow()
        }
    }
}