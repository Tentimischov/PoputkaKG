package baktiyar.com.poputkakg.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ProfileInfo() : Parcelable {

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("is_driver")
    @Expose
    var isDriver: Boolean? = null
    @SerializedName("gender")
    @Expose
    var gender: Int? = null
    @SerializedName("birth_date")
    @Expose
    var birthDate: String? = null
    @SerializedName("photo")
    @Expose
    var photo: Any? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("registered")
    @Expose
    var registered: String? = null
    @SerializedName("deals_count")
    @Expose
    var dealsCount: Int? = null
    @SerializedName("rating")
    @Expose
    var rating: Float? = null
    @SerializedName("history")
    @Expose
    var history: List<History>? = null

    constructor(parcel: Parcel) : this() {
        userId = parcel.readValue(Int::class.java.classLoader) as? Int
        phone = parcel.readString()
        firstName = parcel.readString()
        lastName = parcel.readString()
        isDriver = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        gender = parcel.readValue(Int::class.java.classLoader) as? Int
        birthDate = parcel.readString()
        city = parcel.readString()
        registered = parcel.readString()
        dealsCount = parcel.readValue(Int::class.java.classLoader) as? Int
        rating = parcel.readValue(Float::class.java.classLoader) as? Float
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(userId)
        parcel.writeString(phone)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeValue(isDriver)
        parcel.writeValue(gender)
        parcel.writeString(birthDate)
        parcel.writeString(city)
        parcel.writeString(registered)
        parcel.writeValue(dealsCount)
        parcel.writeValue(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileInfo> {
        override fun createFromParcel(parcel: Parcel): ProfileInfo {
            return ProfileInfo(parcel)
        }

        override fun newArray(size: Int): Array<ProfileInfo?> {
            return arrayOfNulls(size)
        }
    }

}