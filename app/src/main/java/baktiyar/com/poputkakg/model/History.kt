package baktiyar.com.poputkakg.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class History {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("deal")
    @Expose
    var deal: Int? = null
    @SerializedName("grade")
    @Expose
    var grade: Int? = null
    @SerializedName("comment")
    @Expose
    var comment: String? = null
    @SerializedName("grader")
    @Expose
    var grader: String? = null
    @SerializedName("deal_created")
    @Expose
    var dealCreated: String? = null

}