package com.alexvihlayew.espcon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        route()
    }

    private fun route() {
        getCurrentlySignedUser()?.let(fulfill = { user ->
            Log.d("MainActivity", "Logged in as: $user")
        }, reject = {
            Log.d("MainActivity","Need to log in")
        })
    }

    private fun getCurrentlySignedUser(): User? {
        return User("vihlayew.alex@gmail.com","haversin2X")
    }

}
