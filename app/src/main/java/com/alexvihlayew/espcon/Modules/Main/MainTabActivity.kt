package com.alexvihlayew.espcon.Modules.Main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.alexvihlayew.espcon.R
import kotlinx.android.synthetic.main.activity_main_tab.*

class MainTabActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_devices_list -> {
                Log.d("MainTabActivity","Devices list")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add_device -> {
                Log.d("MainTabActivity","Add device")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tab)

        main_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
