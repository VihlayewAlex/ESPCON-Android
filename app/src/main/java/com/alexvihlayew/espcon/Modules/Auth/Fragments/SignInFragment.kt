package com.alexvihlayew.espcon.Modules.Auth.Fragments


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
class SignInFragment : Fragment() {

    private var signInClosure: ((email: String, password: String) -> Unit)? = null
    fun setSignInClosure(closure: ((email: String, password: String) -> Unit)) {
        signInClosure = closure
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onStart() {
        super.onStart()

        configureSignInButton()
    }

    fun configureSignInButton() {
        val button = this.view?.findViewById<Button>(R.id.buttonSignIn)
        button?.setOnClickListener { _ ->
            val email = this.view?.findViewById<EditText>(R.id.emailSignIn)?.text.toString()
            val password = this.view?.findViewById<EditText>(R.id.passwordSignIn)?.text.toString()
            Log.d("SignInFragment", "Captured: $email, $password")
            signInClosure?.invoke(email, password)
        }
    }

}// Required empty public constructor
