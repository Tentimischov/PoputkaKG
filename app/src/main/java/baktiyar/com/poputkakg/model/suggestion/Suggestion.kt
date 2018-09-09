package baktiyar.com.poputkakg.model.suggestion

import com.google.gson.annotations.SerializedName

class Suggestion {
    @SerializedName("id")
     var id :Int? = null

    @SerializedName("route")
    var route :String? = null

    @SerializedName("seats")
    var seats :String? = null

    @SerializedName("status")
    var status :String? = null

    @SerializedName("created")
    var created : String? = null

    @SerializedName("name")
    var name :String? = null


}