package com.alexvihlayew.espcon.Entities

import io.realm.RealmObject

/**
 * Created by alexvihlayew on 16/11/2017.
 */

open class User: RealmObject() {

    var email: String? = null
    var password: String? = null

}