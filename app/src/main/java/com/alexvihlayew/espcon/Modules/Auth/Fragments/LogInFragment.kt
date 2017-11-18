package com.alexvihlayew.espcon.Modules.Auth.Fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.alexvihlayew.espcon.R


/**
 * A simple [Fragment] subclass.
 */
class LogInFragment : Fragment() {

    private var logInClosure: ((email: String, password: String) -> Unit)? = null
    fun setLogInClosure(closure: ((email: String, password: String) -> Unit)) {
        logInClosure = closure
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onStart() {
        super.onStart()

        configureLogInButton()
    }

    fun configureLogInButton() {
        val button = this.view?.findViewById<Button>(R.id.buttonLogIn)
        button?.setOnClickListener { _ ->
            val email = this.view?.findViewById<EditText>(R.id.emailLogIn)?.text.toString()
            val password = this.view?.findViewById<EditText>(R.id.passwordLogIn)?.text.toString()
            Log.d("LogInFragment", "Captured: $email, $password")
            logInClosure?.invoke(email, password)
        }
    }

}// Required empty public constructor
