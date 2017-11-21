package com.alexvihlayew.espcon.Modules.Main.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.alexvihlayew.espcon.R
import com.alexvihlayew.espcon.Services.DatabaseService
import kotlinx.android.synthetic.main.fragment_add_device.*


/**
 * A simple [Fragment] subclass.
 */
class AddDeviceFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_device, container, false)
    }

    override fun onStart() {
        super.onStart()

        configureButtons()
    }

    private fun configureButtons() {
        view?.findViewById<Button>(R.id.buttonSignOut)?.setOnClickListener { _ ->
            signOut()
        }
    }

    private fun signOut() {
        DatabaseService.shared().deleteAll()
        activity.finish()
    }

}// Required empty public constructor
