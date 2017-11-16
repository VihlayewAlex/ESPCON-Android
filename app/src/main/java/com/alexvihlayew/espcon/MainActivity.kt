package com.alexvihlayew.espcon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.realm.Realm

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureRealm()
    }

    override fun onStart() {
        super.onStart()

        route()
    }

    private fun configureRealm()  {
        Realm.init(this)
    }

    private fun route() {
        getCurrentlySignedUser()?.let(fulfill = { user ->
            Log.d("MainActivity", "Logged in as: $user")
        }, reject = {
            Log.d("MainActivity","Need to log in")
        })
    }

    private fun getCurrentlySignedUser(): User? {
        val user = User()
        user.email = "vihlayew.alex@gmail.com"
        user.password = "haversin2X"
        return user
    }

}
