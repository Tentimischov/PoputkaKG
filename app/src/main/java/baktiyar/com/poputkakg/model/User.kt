package baktiyar.com.poputkakg.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


class User {
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("password_repeat")
    @Expose
    var passwordRepeat: String? = null
    @SerializedName("city_id")
    @Expose
    var cityId: Int? = null
    @SerializedName("is_driver")
    @Expose
    var isDriver: Boolean? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("gender")
    @Expose
    var gender: Int? = null
    @SerializedName("birth_date")
    @Expose
    var birthDate: String? = null

}