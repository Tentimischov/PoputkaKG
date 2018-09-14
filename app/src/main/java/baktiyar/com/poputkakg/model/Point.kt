package baktiyar.com.poputkakg.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Point() :Parcelable {
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0!!.writeString(lat)
        p0!!.writeString(lon)
    }


    override fun describeContents(): Int {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR
    }

    @SerializedName("lat")
    @Expose
    var lat: String? = null
    @SerializedName("lon")
    @Expose
    var lon: String? = null

    constructor(parcel: Parcel) : this() {
        lat = parcel.readString()
        lon = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<Point> {
        override fun createFromParcel(parcel: Parcel): Point {
            return Point(parcel)
        }

        override fun newArray(size: Int): Array<Point?> {
            return arrayOfNulls(size)
        }
    }

}