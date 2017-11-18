package com.alexvihlayew.espcon.Entities

import io.realm.RealmObject

/**
 * Created by alexvihlayew on 16/11/2017.
 */

open class RealmUser: RealmObject() {

    var email: String? = null
    var password: String? = null
    var userID: Int? = null
    var name: String? = null

    fun getUser(): User {
        return User().also { usr ->
            usr.email = this.email
            usr.password = this.password
            usr.id = this.userID
            usr.name = this.name
        }
    }

}