package com.alexvihlayew.espcon.Services

import android.util.Log
import com.alexvihlayew.espcon.Entities.ESPCONDevice
import com.alexvihlayew.espcon.Other.*
import com.github.kittinunf.fuel.Fuel
import org.json.JSONObject

/**
 * Created by alexvihlayew on 21/11/2017.
 */
class DevicesService {

    companion object {
        private var sharedInstance = DevicesService()
        fun shared() = sharedInstance
    }

    fun addNew(device: ESPCONDevice, withCompletionHandler: (Int?, Exception?) -> Unit) {
        Fuel.post(addDeviceURL, listOf("user_id" to device.userID, "device_name" to device.deviceName,
                                        "ssid" to device.ssid, "password" to device.password,
                                        "mac_address" to device.MACaddress)).response(handler = { request, response, result ->
            result.fold(success = { data ->
                val jsonString = String(data)
                val obj = JSONObject(jsonString)
                Log.d("DevicesService", obj.toString())
                if (obj.has("id")) {
                    val id = obj.getInt("id")
                    withCompletionHandler.invoke(id, null)
                } else {
                    withCompletionHandler.invoke(null, Exception("Invalid server response"))
                }
            }, failure = { error ->
                withCompletionHandler.invoke(null, error)
            })
        })
    }

    fun getDevicesList(withCompletionHandler: (List<ESPCONDevice>?, Exception?) -> Unit) {
        DatabaseService.shared().getUser()?.id.let(fulfill = { usrID ->
            Fuel.get(allDevicesURL + "?user_id=$usrID").response(handler = { request, response, result ->
                result.fold(success = { data ->
                    val jsonString = String(data).dropLast(1)
                    val obj = JSONObject(jsonString)
                    Log.d("__Device", obj.toString())
                    val devicesArr = obj.getJSONArray("result")
                    var devices = mutableListOf<ESPCONDevice>()
                    for (deviceIndex in 0..(devicesArr.length()-1)) {
                        val deviceObj = devicesArr.getJSONObject(deviceIndex)
                        val device = ESPCONDevice()
                        device.deviceID = deviceObj.getString("device_id").toInt()
                        device.userID = deviceObj.getString("user_id").toInt()
                        device.deviceName = deviceObj.getString("device_name")
                        device.MACaddress = deviceObj.getString("mac_address")
                        device.ssid = deviceObj.getString("wifi_ssid")
                        device.password = deviceObj.getString("wifi_password")
                        device.isOn = deviceObj.getString("device_state")
                        devices.add(device)
                    }
                    withCompletionHandler.invoke(devices, null)
                }, failure = { error ->
                    withCompletionHandler.invoke(null, error)
                })
            })
        }, reject = {
            withCompletionHandler.invoke(null, Exception("Unable to load user data"))
        })
    }

    fun switchStateFor(device: ESPCONDevice, withCompletionHandler: (Exception?) -> Unit) {
        val state = if (device.isOn == "on") { "off" } else { "on" }
        Log.d("DevicesService", state)
        Fuel.post(setState + "?device_id=${device.deviceID}&on_off=$state").response(handler = { request, response, result ->
            result.fold(success = { data ->
                withCompletionHandler.invoke(null)
            }, failure = { error ->
                withCompletionHandler.invoke(error)
            })
        })
    }

    fun delete(device: ESPCONDevice, withCompletionHandler: (Exception?) -> Unit) {
        Fuel.post(deleteDeviceURL + "?device_id=${device.deviceID}").response(handler = { request, response, result ->
            result.fold(success = { data ->
                withCompletionHandler.invoke(null)
            }, failure = { error ->
                withCompletionHandler.invoke(error)
            })
        })
    }

}