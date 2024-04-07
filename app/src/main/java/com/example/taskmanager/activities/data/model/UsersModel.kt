package com.example.taskmanager.activities.data.model

import android.os.Parcel
import android.os.Parcelable

data class UsersModel (
    val userId:String? = "",
    val status:String? = "",
    val imageUrl:String? = "",
    val userName:String? = "",
    val role:String? = ""

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(status)
        parcel.writeString(imageUrl)
        parcel.writeString(userName)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UsersModel> {
        override fun createFromParcel(parcel: Parcel): UsersModel {
            return UsersModel(parcel)
        }

        override fun newArray(size: Int): Array<UsersModel?> {
            return arrayOfNulls(size)
        }
    }
}