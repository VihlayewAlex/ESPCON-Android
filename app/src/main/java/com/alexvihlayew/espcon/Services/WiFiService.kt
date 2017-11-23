package com.alexvihlayew.espcon.Services

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.content.IntentFilter
import android.net.wifi.WifiInfo
import android.net.NetworkInfo
import android.content.Intent
import android.content.BroadcastReceiver
import android.net.ConnectivityManager
import android.util.Log
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.schedule


/**
 * Created by alexvihlayew on 22/11/2017.
 */
class WiFiService {

    companion object {
        private var sharedInstance = WiFiService()
        fun shared() = sharedInstance
        var isRegisteredForWiFiStatusUpdates = false
    }

    fun connectToWiFiDevice(withContext: Context,
                            SSID: String,
                            password: String,
                            userID: String,
                            deviceID: String,
                            server: String,
                            randomUID: String) {
        if (!isRegisteredForWiFiStatusUpdates) {
            val receiver = WifiReceiver()
            receiver._callback = {
                for (i in 0..5) {
                    Timer("WiFiCallback", false).schedule((500 * i).toLong(), action = {
                        doAsync {
                            val requestString = "http://192.168.4.1/ssid_name:" + SSID + ";ssid_pass:" + password + ";user_id:" + userID + ";device_id:" + deviceID + ";server:" + server + ";mac:" + randomUID + ";"
                            val connection = URL(requestString).openConnection() as HttpURLConnection
                            try {
                                val data = connection.inputStream.bufferedReader().readText()
                                uiThread {
                                    Log.d("WiFiService", data)
                                }
                            } finally {
                                Log.d("WiFiService", "Disconnecting..")
                                connection.disconnect()
                            }
                        }
                        if (i == 4) {
                            val wifiManager = withContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                            val networkName = wifiManager.connectionInfo.ssid
                            val networkId = wifiManager.connectionInfo.networkId
                            if (networkName == "ESP32DEV") {
                                wifiManager.removeNetwork(networkId)
                                wifiManager.saveConfiguration()
                            }
                        }
                    })
                }
            }
            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            withContext.registerReceiver(receiver, intentFilter)
        }

        val networkSSID = "ESP32DEV"
        val networkPass = "12345678"

        val conf = WifiConfiguration()
        conf.SSID = "\"" + networkSSID + "\""
        conf.preSharedKey = "\""+ networkPass +"\""

        val wifiManager = withContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.addNetwork(conf)

        val list = wifiManager.configuredNetworks
        for (i in list) {
            if (i.SSID != null && i.SSID == "\"" + networkSSID + "\"") {
                wifiManager.disconnect()
                wifiManager.enableNetwork(i.networkId, true)
                wifiManager.reconnect()

                break
            }
        }
    }

    class WifiReceiver() : BroadcastReceiver() {

        var _callback: (()->Unit)? = null

        override fun onReceive(context: Context, intent: Intent) {

            val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
            if (info != null && info.isConnected) {

                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                val ssid = wifiInfo.ssid
                if (ssid == "\"ESP32DEV\"") {
                    Log.d("WiFiService", "SUCCESS!!!")
                    _callback?.invoke()
                } else {
                    Log.d("WiFiService", "BULLSHIT!!! : $ssid")
                }
            }
        }
    }

}