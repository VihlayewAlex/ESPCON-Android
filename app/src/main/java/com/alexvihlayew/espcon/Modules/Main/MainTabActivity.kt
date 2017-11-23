package com.alexvihlayew.espcon.Modules.Main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.alexvihlayew.espcon.Modules.Auth.AuthTabActivity
import com.alexvihlayew.espcon.Modules.Auth.Fragments.LogInFragment
import com.alexvihlayew.espcon.Modules.Auth.Fragments.SignInFragment
import com.alexvihlayew.espcon.Modules.Main.Fragments.AddDeviceFragment
import com.alexvihlayew.espcon.Modules.Main.Fragments.DevicesListFragment
import com.alexvihlayew.espcon.R
import kotlinx.android.synthetic.main.activity_main_tab.*

class MainTabActivity : AppCompatActivity() {

    enum class MainFragment {
        DEVICES_LIST, ADD_DEVICE
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_devices_list -> {
                Log.d("MainTabActivity","Devices list")
                switchToFragment(MainFragment.DEVICES_LIST)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add_device -> {
                Log.d("MainTabActivity","Add device")
                switchToFragment(MainFragment.ADD_DEVICE)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tab)

        main_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        configureInitialFragment()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    private fun configureInitialFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.main_frame_layout, fragmentOfType(MainTabActivity.MainFragment.DEVICES_LIST))
        fragmentTransaction.commit()
        supportActionBar?.title = "Devices list"
    }

    private fun switchToFragment(newFragment: MainTabActivity.MainFragment) {
        val fragmentManager = supportFragmentManager
        val targetFragment = fragmentOfType(newFragment)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame_layout, targetFragment)
        fragmentTransaction.commit()
        supportActionBar?.title = when (newFragment) {
            MainTabActivity.MainFragment.DEVICES_LIST -> "Devices list"
            MainTabActivity.MainFragment.ADD_DEVICE -> "Add device"
        }
    }

    private fun fragmentOfType(type: MainTabActivity.MainFragment): Fragment {
        return when (type) {
            MainTabActivity.MainFragment.DEVICES_LIST -> DevicesListFragment().also { fragment ->
                // setup
            }
            MainTabActivity.MainFragment.ADD_DEVICE -> AddDeviceFragment().also { fragment ->
                // setup
            }
        }

    }

}
