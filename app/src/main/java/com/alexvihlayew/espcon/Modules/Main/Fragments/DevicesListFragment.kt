package com.alexvihlayew.espcon.Modules.Main.Fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.alexvihlayew.espcon.R
import com.alexvihlayew.espcon.Services.DevicesService


/**
 * A simple [Fragment] subclass.
 */
class DevicesListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices_list, container, false)
    }

    override fun onStart() {
        super.onStart()

        configureListView()
        loadData()
    }

    private fun configureListView() {
        val listView = view?.findViewById<ListView>(R.id.devices_list_view)
        Log.d("DevicesListFragment", "$listView")
        listView?.adapter = DevicesListAdapter(activity)
    }

    private fun loadData() {
        // TODO
    }


    private class DevicesListAdapter(context: Context): BaseAdapter() {

        private val _context: Context

        init {
            _context = context
        }

        override fun getCount(): Int {
            return 10
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
            deviceName.text = position.toString()
            stateSwitch.setOnClickListener { _ ->
                Log.d("DeviceListFragment", "About to toggle state of device $position")
            }
            delButton.setOnClickListener { _ ->
                Log.d("DeviceListFragment", "About to delete device $position")
            }
            return cell
        }

    }

}
