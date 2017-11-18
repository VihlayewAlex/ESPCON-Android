package com.alexvihlayew.espcon.Modules.Auth

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.alexvihlayew.espcon.Modules.Auth.Fragments.LogInFragment
import com.alexvihlayew.espcon.Modules.Auth.Fragments.SignInFragment
import com.alexvihlayew.espcon.Other.let
import com.alexvihlayew.espcon.R
import com.alexvihlayew.espcon.Services.AuthService
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
        supportActionBar?.title = "Log In"
    }

    private fun switchToFragment(newFragment: AuthFragment) {
        val fragmentManager = supportFragmentManager
        val targetFragment = fragmentOfType(newFragment)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.auth_frame_layout, targetFragment)
        fragmentTransaction.commit()
        supportActionBar?.title = when (newFragment) {
            AuthFragment.LOG_IN -> "Log In"
            AuthFragment.SIGN_IN -> "Sign In"
        }
    }

    private fun fragmentOfType(type: AuthFragment): Fragment {
        return when (type) {
            AuthFragment.LOG_IN -> LogInFragment().also { fragment ->
                fragment.setLogInClosure(closure = { email, password ->
                    logInWith(email, password)
                })
            }
            AuthFragment.SIGN_IN -> SignInFragment().also { fragment ->
                fragment.setSignInClosure(closure = { name, email, password ->
                    signInWith(name, email, password)
                })
            }
        }

    }

    private fun logInWith(email: String, password: String) {
        Log.d("AuthTabActivity", "Logging in with $email, $password")
        AuthService.shared().logInWith(email, password, completionHandler = { userInfo, exception ->
            userInfo.let(fulfill = { info ->
                Log.d("AuthTabActivity","Going back..")
                finish()
            }, reject = {
                exception?.let { except ->
                    val alert = AlertDialog.Builder(this@AuthTabActivity).create()
                    alert.setTitle("Error")
                    alert.setMessage(except.message)
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", { _, _ -> })
                    alert.show()
                }
            })
        })
    }

    private fun signInWith(name: String, email: String, password: String) {
        Log.d("AuthTabActivity", "Signing in with $email, $password")
        AuthService.shared().signInWith(name, email, password, completionHandler = { fuelError ->
            fuelError.let(fulfill = { err ->
                val alert = AlertDialog.Builder(this@AuthTabActivity).create()
                alert.setTitle("Error")
                alert.setMessage(err.message)
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", { _, _ -> })
                alert.show()
            }, reject = {
                this.logInWith(email, password)
            })
        })
    }

}
