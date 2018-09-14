package baktiyar.com.poputkakg.ui.suggestions

import android.content.Context
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.model.suggestion.Suggestion
import baktiyar.com.poputkakg.model.suggestion.SuggestionList
import baktiyar.com.poputkakg.util.IProgressBar

interface  SuggestionContract {
    interface View : IProgressBar{
        fun onSuccessGetDeals(list:SuggestionList)
        fun onSuccessGetDealsWithClosestDepartureTime(list:ArrayList<Suggestion>)
        fun onSuccessGetOwnRoutes(list:ArrayList<Suggestion>)
    }
    interface Presenter{
        fun getDeals(context: Context, token:String?)
        fun getDealsWithClosestDepartureTime(context:Context,token:String?)
        fun getOwnRoutes(context:Context,token:String?)
    }

}