package com.alexvihlayew.espcon.Services

import android.support.annotation.MainThread
import android.util.Log
import com.alexvihlayew.espcon.Entities.User
import com.alexvihlayew.espcon.Entities.UserInfo
import com.alexvihlayew.espcon.Other.logInURL
import com.alexvihlayew.espcon.Other.signInURL
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import org.json.JSONObject

/**
 * Created by alexvihlayew on 18/11/2017.
 */
class AuthService {

    companion object {
        private var sharedInstance = AuthService()
        fun shared() = sharedInstance
    }

    fun signInWith(name: String, email: String, password: String, completionHandler: (Exception?) -> Unit) {
        Fuel.post(signInURL, listOf("name" to name, "email" to email, "password" to password)).response(handler = { request, response, result ->
            result.fold(success = { data ->
                val jsonString = String(data)
                val obj = JSONObject(jsonString)
                val status = obj.getString("status")
                val message = obj.getString("msg")
                if (status == "success") {
                    val id = obj.getInt("id")
                    val user = User().also { usr ->
                        usr.id = id
                        usr.email = email
                        usr.password = password
                        usr.name = name
                    }
                    DatabaseService.shared().saveUser(user)
                    completionHandler.invoke(null)
                } else {
                    val exception = Exception(message)
                    completionHandler.invoke(exception)
                }
            }, failure = { error ->
                completionHandler.invoke(error)
            })
        })
    }

    fun logInWith(email: String, password: String, completionHandler: (User?, Exception?) -> Unit) {
        Fuel.post(logInURL, listOf("username" to email,"password" to password)).response(handler = { request, response, result ->
            result.fold(success = { data ->
                val jsonString = String(data)
                val obj = JSONObject(jsonString)
                val status = obj.getString("status")
                val message = obj.getString("msg")
                if (status == "success") {
                    Log.d("AuthService", "status is success")
                    val userInfo = obj.getJSONObject("user_info")
                    val userID = userInfo.getInt("user_id")
                    val userName = userInfo.getString("user_name")
                    val userEmail = userInfo.getString("user_email")
                    val password = userInfo.getString("password")
                    val otpCode = userInfo.getString("otp_code")
                    val access = userInfo.getString("access")
                    val usr = UserInfo().also { info ->
                        info.userID = userID
                        info.userName = userName
                        info.userEmail = userEmail
                        info.userPassword = password
                        info.otpCode = otpCode
                        info.access = access
                    }.toUser()
                    DatabaseService.shared().saveUser(usr)
                    completionHandler.invoke(usr, null)
                } else {
                    Log.d("AuthService", "status is NOT success")
                    val exception = Exception(message)
                    completionHandler.invoke(null, exception)
                }
            }, failure = { err ->
                completionHandler.invoke(null, err)
            })
        })
    }

}