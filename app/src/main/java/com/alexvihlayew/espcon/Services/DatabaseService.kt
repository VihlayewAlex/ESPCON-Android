package com.alexvihlayew.espcon.Services

import android.util.Log
import com.alexvihlayew.espcon.Entities.User
import com.alexvihlayew.espcon.Entities.RealmUser
import com.alexvihlayew.espcon.Entities.RealmUserInfo
import com.alexvihlayew.espcon.Entities.UserInfo
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
        deleteUser()
        Log.d("DatabaseService", "Saving user..")
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val userToSave = Realm.getDefaultInstance().createObject(RealmUser::class.java).also { usr ->
                usr.userID = user.id
                usr.name = user.name
                usr.email = user.email
                usr.password = user.password
            }
        }
        realm.close()
        Log.d("DatabaseService", "Saved user")
    }

    fun getUser(): User? {
        Log.d("DatabaseService", "Getting user..")
        val realm = Realm.getDefaultInstance()
        val user = realm.where(RealmUser::class.java).findFirst()?.getUser()
        realm.close()
        Log.d("DatabaseService", "Get user")
        return user
    }

    fun deleteUser() {
        Log.d("DatabaseService", "Deleting user..")
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            Realm.getDefaultInstance().delete(RealmUser::class.java)
        }
        realm.close()
        Log.d("DatabaseService", "Deleted user")
    }


    fun saveUserInfo(userInfo: UserInfo) {
        deleteUserInfo()
        Log.d("DatabaseService", "Saving user info..")
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val userInfoToSave = Realm.getDefaultInstance().createObject(RealmUserInfo::class.java).also { info ->
                info.userID = userInfo.userID
                info.userName = userInfo.userName
                info.userEmail = userInfo.userEmail
                info.userPassword = userInfo.userPassword
                info.otpCode = userInfo.otpCode
                info.access = userInfo.access
            }
        }
        realm.close()
        Log.d("DatabaseService", "Saved user info")
    }

    fun getUserInfo(): UserInfo? {
        Log.d("DatabaseService", "Getting user info..")
        val realm = Realm.getDefaultInstance()
        val userInfo = realm.where(RealmUserInfo::class.java).findFirst()?.getUserInfo()
        realm.close()
        Log.d("DatabaseService", "Get user info")
        return userInfo
    }

    fun deleteUserInfo() {
        Log.d("DatabaseService", "Deleting user info..")
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            Realm.getDefaultInstance().delete(RealmUserInfo::class.java)
        }
        realm.close()
        Log.d("DatabaseService", "Deleted user info")
    }


    fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            Realm.getDefaultInstance().deleteAll()
        }
        realm.close()
    }

}