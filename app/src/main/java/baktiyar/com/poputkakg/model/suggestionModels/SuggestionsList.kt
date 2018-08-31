package baktiyar.com.poputkakg.model.suggestionModels

import baktiyar.com.poputkakg.model.suggestionModels.Suggestion
import com.google.gson.annotations.SerializedName

class SuggestionsList{
    @SerializedName("outgoing")
    val outgoingList:ArrayList<Suggestion>? = null
    @SerializedName("incoming")
    val incomingList:ArrayList<Suggestion>? = null
}
