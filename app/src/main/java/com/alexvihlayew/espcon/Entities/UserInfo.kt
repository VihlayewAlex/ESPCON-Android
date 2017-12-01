package com.alexvihlayew.espcon.Entities

import android.hardware.usb.UsbRequest

/**
 * Created by alexvihlayew on 18/11/2017.
 */
class UserInfo {

    var userID: Int? = null
    var userName: String? = null
    var userEmail: String? = null
    var userPassword: String? = null
    var otpCode: String? = null
    var access: String? = null

    fun toUser(): User {
        return User().also { usr ->
            usr.id = userID
            usr.name = userName
            usr.email = userEmail
            usr.password = userPassword
        }
    }

}