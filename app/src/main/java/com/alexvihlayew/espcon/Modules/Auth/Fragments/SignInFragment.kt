package com.alexvihlayew.espcon.Modules.Auth.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.alexvihlayew.espcon.Modules.Auth.AuthTabActivity

import com.alexvihlayew.espcon.R


/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {

    private var signInClosure: ((name: String, email: String, password: String) -> Unit)? = null
    fun setSignInClosure(closure: ((name: String, email: String, password: String) -> Unit)) {
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
            var name = this.view?.findViewById<EditText>(R.id.nameSignIn)?.text.toString()
            val email = this.view?.findViewById<EditText>(R.id.emailSignIn)?.text.toString()
            val password = this.view?.findViewById<EditText>(R.id.passwordSignIn)?.text.toString()
            var confirmPassword = this.view?.findViewById<EditText>(R.id.confirmPasswordSignIn)?.text.toString()

            if (password != confirmPassword) {
                val alert = AlertDialog.Builder(activity).create()
                alert.setTitle("Invalid input")
                alert.setMessage("Passwords must match")
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", { _, _ -> })
                alert.show()
            } else if (name == "" || email == "" || password == "" || confirmPassword == "") {
                val alert = AlertDialog.Builder(activity).create()
                alert.setTitle("Invalid input")
                alert.setMessage("Fields cannot be empty")
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", { _, _ -> })
                alert.show()
            } else {
                Log.d("SignInFragment", "Captured: $name, $email, $password")
                signInClosure?.invoke(name, email, password)
            }
        }
    }

}// Required empty public constructor
