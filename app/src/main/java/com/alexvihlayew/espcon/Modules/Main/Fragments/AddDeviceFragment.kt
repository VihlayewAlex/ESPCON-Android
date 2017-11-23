package com.alexvihlayew.espcon.Modules.Main.Fragments



import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.alexvihlayew.espcon.Entities.ESPCONDevice
import com.alexvihlayew.espcon.Other.let

import com.alexvihlayew.espcon.R
import com.alexvihlayew.espcon.Services.DatabaseService
import com.alexvihlayew.espcon.Services.DevicesService
import com.alexvihlayew.espcon.Services.WiFiService

import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class AddDeviceFragment : Fragment() {


    var deviceUIDs = hashMapOf<Int, String>()

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
        view?.findViewById<Button>(R.id.getIDButton)?.setOnClickListener { _ ->
            getID()
        }
        view?.findViewById<Button>(R.id.buttonSaveToBoard)?.setOnClickListener { _ ->
            saveToBoard()
        }
        view?.findViewById<Button>(R.id.buttonSignOut)?.setOnClickListener { _ ->
            signOut()
        }
    }

    private fun getID() {
        val ssid = view?.findViewById<TextView>(R.id.fieldSSID)?.text.toString()
        val password = view?.findViewById<TextView>(R.id.fieldPassword)?.text.toString()
        val confirmPassword = view?.findViewById<TextView>(R.id.fieldConfirmPassword)?.text.toString()
        val deviceName = view?.findViewById<TextView>(R.id.fieldDeviceName)?.text.toString()

        if (password != confirmPassword) {
            val alert = AlertDialog.Builder(activity).create()
            alert.setTitle("Invalid input")
            alert.setMessage("Passwords must match")
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", { _, _ -> })
            alert.show()
        } else if (ssid == "" || deviceName == "") {
            val alert = AlertDialog.Builder(activity).create()
            alert.setTitle("Invalid input")
            alert.setMessage("Fields cannot be empty")
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", { _, _ -> })
            alert.show()
        }

        val device = ESPCONDevice()
            device.userID = DatabaseService.shared().getUser()?.id
            device.ssid = ssid
            device.password = password
            device.deviceName = deviceName
        val UUID = getRandomUUID()
            device.MACaddress = UUID // TODO: REPLACE
        DevicesService.shared().addNew(device, withCompletionHandler = { id, exception ->
            id.let(fulfill = { deviceID ->
                deviceUIDs.set(deviceID, UUID)
                view?.findViewById<TextView>(R.id.fieldDeviceID)?.text = id.toString()
            }, reject = {
                exception?.let { error ->
                    Log.d("AddDeviceFragment", error.message)
                }
            })
        })
    }

    fun getRandomUUID(): String {
        val SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < 10) {
            val index = (rnd.nextFloat() * SALTCHARS.length).toInt()
            salt.append(SALTCHARS.toCharArray().get(index))
        }
        return salt.toString()
    }

    private fun saveToBoard() {
        val ssid = view?.findViewById<TextView>(R.id.fieldSSID)?.text.toString()
        val password = view?.findViewById<TextView>(R.id.fieldPassword)?.text.toString()
        val userID = DatabaseService.shared().getUser()?.id.toString()
        val deviceID = view?.findViewById<TextView>(R.id.fieldDeviceID)?.text.toString()

        if (ssid == "" || deviceID == "") {
            val alert = AlertDialog.Builder(activity).create()
            alert.setTitle("Invalid input")
            alert.setMessage("Fields cannot be empty")
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", { _, _ -> })
            alert.show()
            return
        }

        val server = "gold2star.kjbsoft.com"
        val randomUID = deviceUIDs.getValue(deviceID.toInt())

        WiFiService.shared().connectToWiFiDevice(activity, ssid, password, userID, deviceID, server, randomUID)
    }

    private fun signOut() {
        DatabaseService.shared().deleteAll()
        activity.finish()
    }

}// Required empty public constructor
