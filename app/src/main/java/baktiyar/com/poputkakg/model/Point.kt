package baktiyar.com.poputkakg.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Point {

    @SerializedName("lat")
    @Expose
    var lat: String? = null
    @SerializedName("lon")
    @Expose
    var lon: String? = null

}