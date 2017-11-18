package com.alexvihlayew.espcon.Modules

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alexvihlayew.espcon.Modules.Auth.AuthTabActivity
import com.alexvihlayew.espcon.Other.let
import com.alexvihlayew.espcon.Modules.Main.MainTabActivity
import com.alexvihlayew.espcon.R
import com.alexvihlayew.espcon.Entities.User
import com.alexvihlayew.espcon.Services.DatabaseService
import io.realm.Realm
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureRealm()
    }

    override fun onStart() {
        super.onStart()

        Timer().schedule(timerTask { route() }, 2000)
    }

    private fun configureRealm()  {
        Realm.init(this)
    }

    private fun route() {
        DatabaseService.shared().getUser().let(fulfill = { user ->
            Log.d("MainActivity", "Logged in as: ${user.email}")
            routeToMain(user)
        }, reject = {
            Log.d("MainActivity","Need to log in")
            routeToAuth()
        })
    }

    private fun routeToMain(user: User) {
        Log.d("MainActivity","Routing to Main..")
        val intent = Intent(this@MainActivity, MainTabActivity::class.java)
        startActivity(intent)
    }

    private fun routeToAuth() {
        Log.d("MainActivity","Routing to Auth..")
        val intent = Intent(this@MainActivity, AuthTabActivity::class.java)
        startActivity(intent)
    }

}
