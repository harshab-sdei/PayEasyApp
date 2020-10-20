package com.peazyapp.peazy.controllers.ui.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.peazyapp.peazy.R

class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MenuFragment.newInstance())
                .commitNow()
        }
    }
}