package baktiyar.com.poputkakg.model.suggestion

import com.google.gson.annotations.SerializedName

class SuggestionList {
    @SerializedName("outgoing")
    public var outgoingList:ArrayList<Suggestion>? = null

    @SerializedName("incoming")
    public var incomingList : ArrayList<Suggestion>? = null
}