package baktiyar.com.poputkakg.ui.suggestions

import android.content.Context
import baktiyar.com.poputkakg.model.suggestion.SuggestionList
import baktiyar.com.poputkakg.util.IProgressBar

interface  SuggestionContract {
    interface View : IProgressBar{
        fun onSuccessGetDeals(list:SuggestionList)
    }
    interface Presenter{
        fun getDeals(context: Context, token:String?)
    }

}