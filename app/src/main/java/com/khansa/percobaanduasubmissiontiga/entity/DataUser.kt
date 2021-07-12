package com.khansa.percobaanduasubmissiontiga.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataUser(
    var id: Int = 0,
    var photo: String? = "",
    var username: String? = "",
    var follower: Int = 0,
    var following: Int = 0
) : Parcelable
