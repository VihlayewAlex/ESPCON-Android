package com.alexvihlayew.espcon.Modules.Main.Fragments


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alexvihlayew.espcon.Entities.ESPCONDevice

import com.alexvihlayew.espcon.R
import com.alexvihlayew.espcon.Services.DevicesService


/**
 * A simple [Fragment] subclass.
 */
class DevicesListFragment : Fragment() {

    var listView: ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices_list, container, false)
    }

    override fun onResume() {
        super.onResume()

        configureListView()
        DevicesService.shared().getDevicesList(withCompletionHandler = { list, exception ->
            exception?.let {
                Log.d("DevicesListFragment", exception.message)
                val alert = AlertDialog.Builder(activity).create()
                alert.setTitle("Error loading devices list")
                alert.setMessage(exception.message)
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", { _, _ -> })
                alert.show()
            }
            list?.let {
                Log.d("DevicesListFragment", "Displaying devices")
                loadDevicesList(list)
            }
        })
    }

    private fun configureListView() {
        listView = view?.findViewById<ListView>(R.id.devices_list_view)
        listView?.adapter = DevicesListAdapter(activity)
    }

    private fun reloadData() {
        val adapter = listView?.adapter as DevicesListAdapter
        adapter?.let { adptr ->
            adptr.notifyDataSetChanged()
        }
    }

    private fun loadDevicesList(devicesList: List<ESPCONDevice>) {
        Log.d("DevicesListFragment", "Should load ${devicesList.count()} devices")
        (listView?.adapter as DevicesListAdapter)?.loadDevices(devicesList)
        reloadData()
    }


    private class DevicesListAdapter(context: Context): BaseAdapter() {

        private val _context: Context
        private var devicesList: List<ESPCONDevice> = listOf()

        fun loadDevices(devices: List<ESPCONDevice>) {
            devicesList = devices
        }

        init {
            _context = context
        }

        override fun getCount(): Int {
            return devicesList.filter { it.isOn != "del" }.count()
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return "Test string"
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(_context)
            val cell = layoutInflater.inflate(R.layout.device_cell, viewGroup, false)
            val deviceName = cell.findViewById<TextView>(R.id.device_name_text_view)
            val stateSwitch = cell.findViewById<Switch>(R.id.device_state_switch)
            val delButton = cell.findViewById<Button>(R.id.delete_device_button)
            val device = devicesList.filter { it.isOn != "del" }.get(position)
            deviceName.text = device.deviceName
            stateSwitch.isChecked = (device.isOn == "on")
            stateSwitch.setOnClickListener { _ ->
                Log.d("DeviceListFragment", "About to toggle state of device $position")
                DevicesService.shared().switchStateFor(device, withCompletionHandler = { error ->
                    Log.d("DevicesListFragment", error?.message ?: "Set state success to ${stateSwitch.isChecked}")
                    device.isOn = if (device.isOn == "on") { "off" } else { "on" }
                    this.notifyDataSetChanged()
                })
            }
            delButton.setOnClickListener { _ ->
                Log.d("DeviceListFragment", "About to delete device $position")
                val alert = AlertDialog.Builder(_context).create()
                alert.setTitle("Delete")
                alert.setMessage("Are you sure?")
                alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Delete", { _, _ ->
                    DevicesService.shared().delete(device, withCompletionHandler = { error ->
                        Log.d("DevicesListFragment", error?.message ?: "Deleted successfully")
                        devicesList = devicesList.filter { it.deviceID != device.deviceID }
                        this.notifyDataSetChanged()
                    })
                })
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", { _, _ -> })
                alert.show()
            }
            return cell
        }

    }

}
