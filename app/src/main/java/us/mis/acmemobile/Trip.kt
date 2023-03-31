package us.mis.acmemobile

import android.os.Parcel
import android.os.Parcelable
import java.util.Calendar

data class Trip (val id: String,val origin: String, val destination: String, val description: String, val startDate: Calendar, val endDate: Calendar, val price: Double, val imgURL: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        (parcel.readSerializable() as Calendar),
        (parcel.readSerializable() as Calendar),
        parcel.readDouble(),
        parcel.readString()!!
    ) {
    }

    constructor() : this("","", "", "", Calendar.getInstance(), Calendar.getInstance(), 0.0, "")

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(id)
        p0.writeString(origin)
        p0.writeString(destination)
        p0.writeString(description)
        p0.writeSerializable(startDate)
        p0.writeSerializable(endDate)
        p0.writeDouble(price)
        p0.writeString(imgURL)
    }

    companion object CREATOR : Parcelable.Creator<Trip> {
        override fun createFromParcel(parcel: Parcel): Trip {
            return Trip(parcel)
        }

        override fun newArray(size: Int): Array<Trip?> {
            return arrayOfNulls(size)
        }
    }
}