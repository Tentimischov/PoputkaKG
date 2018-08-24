package baktiyar.com.poputkakg.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Rout {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("owner")
    @Expose
    var owner: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("available_seats")
    @Expose
    var availableSeats: Int? = null
    @SerializedName("isDriver")
    @Expose
    var isDriver: Boolean? = null
    @SerializedName("isLocal")
    @Expose
    var isLocal: Boolean? = null
    @SerializedName("isBag")
    @Expose
    var isBag: Boolean? = null
    @SerializedName("start_address")
    @Expose
    var startAddress: String? = null
    @SerializedName("end_address")
    @Expose
    var endAddress: String? = null
    @SerializedName("start_latitude")
    @Expose
    var startLatitude: String? = null
    @SerializedName("start_longitude")
    @Expose
    var startLongitude: String? = null
    @SerializedName("end_latitude")
    @Expose
    var endLatitude: String? = null
    @SerializedName("end_longitude")
    @Expose
    var endLongitude: String? = null
    @SerializedName("start_time")
    @Expose
    var startTime: Int? = null
    @SerializedName("points")
    @Expose
    var points: List<Point>? = null

}