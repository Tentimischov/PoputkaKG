package baktiyar.com.poputkakg.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SmsCode {

    @SerializedName("sms_code")
    @Expose
    var smsCode: Int? = null
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

}