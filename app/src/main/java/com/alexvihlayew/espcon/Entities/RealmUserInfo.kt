package com.alexvihlayew.espcon.Entities

import io.realm.RealmObject

/**
 * Created by alexvihlayew on 18/11/2017.
 */
open class RealmUserInfo: RealmObject() {

    var userID: String? = null
    var userName: String? = null
    var userEmail: String? = null
    var userPassword: String? = null
    var otpCode: String? = null
    var access: String? = null

    fun getUserInfo(): UserInfo {
        return UserInfo().also { info ->
            info.userID = this.userID
            info.userName = this.userName
            info.userEmail = this.userEmail
            info.userPassword = this.userPassword
            info.otpCode = this.otpCode
            info.access = this.access
        }
    }

}