package baktiyar.com.poputkakg.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Login {

    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null

}