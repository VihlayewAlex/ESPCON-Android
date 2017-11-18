package com.alexvihlayew.espcon.Modules.Auth

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.alexvihlayew.espcon.Modules.Auth.Fragments.LogInFragment
import com.alexvihlayew.espcon.Modules.Auth.Fragments.SignInFragment
import com.alexvihlayew.espcon.R
import kotlinx.android.synthetic.main.activity_auth_tab.*

class AuthTabActivity : AppCompatActivity() {

    enum class AuthFragment {
        SIGN_IN, LOG_IN
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_log_in -> {
                Log.d("AuthTabActivity","Log In")
                switchToFragment(AuthFragment.LOG_IN)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sign_in -> {
                Log.d("AuthTabActivity","Sign In")
                switchToFragment(AuthFragment.SIGN_IN)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_tab)

        auth_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        configureInitialFragment()
    }

    private fun configureInitialFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.auth_frame_layout, fragmentOfType(AuthFragment.LOG_IN))
        fragmentTransaction.commit()
    }

    private fun switchToFragment(newFragment: AuthFragment) {
        val fragmentManager = supportFragmentManager
        val targetFragment = fragmentOfType(newFragment)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.auth_frame_layout, targetFragment)
        fragmentTransaction.commit()
    }

    private fun fragmentOfType(type: AuthFragment): Fragment {
        return when (type) {
            AuthFragment.LOG_IN -> LogInFragment().also { fragment ->
                fragment.setLogInClosure(closure = { email, password ->
                    Log.d("AuthTabActivity", "Logging in with $email, $password")
                })
            }
            AuthFragment.SIGN_IN -> SignInFragment().also { fragment ->
                fragment.setSignInClosure(closure = { email, password ->
                    Log.d("AuthTabActivity", "Signing in with $email, $password")
                })
            }
        }

    }

}
