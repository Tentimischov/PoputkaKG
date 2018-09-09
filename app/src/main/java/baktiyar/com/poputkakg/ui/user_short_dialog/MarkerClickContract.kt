package baktiyar.com.poputkakg.ui.user_short_dialog

import android.content.Context
import baktiyar.com.poputkakg.model.DealBody
import okhttp3.ResponseBody

interface MarkerClickContract{
    interface View{
        fun onSuccessOfferDeal(dealUser: DealBody?)
        fun onError(message:String)
        fun onFailureOfferDeal(body:ResponseBody?)
    }
    interface Presenter{
        fun offerDeal(routId:Int?,context: Context)
    }
}