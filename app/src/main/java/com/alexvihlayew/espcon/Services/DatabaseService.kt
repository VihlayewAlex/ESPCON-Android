package com.alexvihlayew.espcon.Services

import android.util.Log
import com.alexvihlayew.espcon.Entities.User
import com.alexvihlayew.espcon.Entities.RealmUser
import com.alexvihlayew.espcon.Other.let
import io.realm.Realm

/**
 * Created by alexvihlayew on 18/11/2017.
 */
class DatabaseService {

    companion object {
        private var sharedInstance = DatabaseService()
        fun shared() = sharedInstance
    }

    fun saveUser(user: User) {
        //deleteUser()
        Log.d("DatabaseService", "Saving user..")
        Realm.getDefaultInstance().executeTransaction {
            val userToSave = Realm.getDefaultInstance().createObject(RealmUser::class.java).also { usr ->
                usr.userID = user.id
                usr.name = user.name
                usr.email = user.email
                usr.password = user.password
            }
        }
        Log.d("DatabaseService", "Saved user")
    }

    fun getUser(): User? {
        Log.d("DatabaseService", "Getting user..")
        val user = Realm.getDefaultInstance().where(RealmUser::class.java).findFirst()?.getUser()
        Log.d("DatabaseService", "Get user")
        return user
    }

    fun deleteUser() {
        Log.d("DatabaseService", "Deleting user..")
        Realm.getDefaultInstance().executeTransaction {
            Realm.getDefaultInstance().deleteAll()
        }
        Log.d("DatabaseService", "Deleted user")
    }

}