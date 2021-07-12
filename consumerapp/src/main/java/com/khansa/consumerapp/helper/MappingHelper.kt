package com.khansa.consumerapp.helper

import android.database.Cursor

import com.khansa.consumerapp.db.DatabaseContract
import com.khansa.consumerapp.entity.DataUser
import java.lang.Exception

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<DataUser> {
        val userList = ArrayList<DataUser>()
        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))

                userList.add(DataUser(id = id, username = username, photo = photo))
            }
        }
        return userList
    }

    fun mapCursorToObject(userCursor: Cursor?): DataUser {
        var user = DataUser()
        try {
            userCursor?.apply {
                moveToFirst()
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
                user = DataUser(
                    id = id,
                    username = username,
                    photo = photo)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return user
    }
}