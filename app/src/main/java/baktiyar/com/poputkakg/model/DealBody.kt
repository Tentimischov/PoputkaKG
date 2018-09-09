package baktiyar.com.poputkakg.model

import com.google.gson.annotations.SerializedName

class DealBody(id:Int?,route:Int?,seats:Int?,status:Int?){
    @SerializedName("id")
    var id :Int? = null
    @SerializedName("route")
    var route :Int? = null
    @SerializedName("seats")
    var seats :Int? = null
    @SerializedName("status")
    var status :Int? = null
    @SerializedName("dialog")
    var message:String? = null
}