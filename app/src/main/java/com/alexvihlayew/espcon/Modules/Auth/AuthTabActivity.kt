package com.alexvihlayew.espcon.Modules.Auth

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.alexvihlayew.espcon.Modules.Auth.Fragments.LogInFragment
import com.alexvihlayew.espcon.R
import kotlinx.android.synthetic.main.activity_auth_tab.*

class AuthTabActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_log_in -> {
                Log.d("AuthTabActivity","Log In")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sign_in -> {
                Log.d("AuthTabActivity","Sign In")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_tab)

        auth_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        configureFragment()
    }

    private fun configureFragment() {
        
    }
}
